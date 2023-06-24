package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.transaction.Transaction;
import com.alexpages.ebankingapi.utils.PaginatedList;
import com.alexpages.ebankingapi.utils.TransactionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    //Acts as a publisher and a subscriber(consumer)
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AccountService accountService;
    private final ClientService clientService;
    private final Calendar calendar = Calendar.getInstance();
    private final Consumer<String, String> kafkaConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RequestControlService requestControlService;
    private final ExchangeRateService exchangeRateService;

    //PUBLISHER
    public Transaction publishTransactionToTopic(Transaction transaction){
        //Schema: topic = year+client, partition = month -> 12 topics per client
        String clientName = accountService
                .getClientByAccount(transaction.getIban())
                .getUsername();

        //Get transaction date and obtain topic to be published
        calendar.setTime(transaction.getDate());
        int transactionPartitionMonth = calendar.get(Calendar.MONTH);                   //will be the partition of the topic
        int transactionYear = calendar.get(Calendar.YEAR);                              //will help define the topic
        String transactionTopic = "transactions-" + transactionYear + "-" + clientName; //topic

        try {
            String messageKey = transaction.getId();
            String messageValue = objectMapper.writeValueAsString(transaction);
            //Send transaction to topic
            kafkaTemplate.send(transactionTopic, transactionPartitionMonth, messageKey, messageValue);
            return transaction;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);          //Always runtime exception
        }
    }

    //CONSUMER
    public TransactionResponse getPaginatedTransactionListByUserAndDate(int pageSize, String clientName, int year, int month){
        List<Transaction> transactionList = consumeTransactionsFromTopic(clientName,year,month);
        int transactionAmount = transactionList.size();
        int requiredPages = (int) Math.ceil((double) transactionAmount / pageSize);

        List<PaginatedList> paginatedLists = new ArrayList<>();
        int beginning = 0;
        int end = pageSize;

        //Windowed approach
        for (int i = 0; i< requiredPages; i++){
            List<Transaction> transactionsOfPage = transactionList.subList(beginning, Math.min(end, transactionAmount));
            int currentPageNo = i+1;
            PaginatedList paginatedList = PaginatedList.builder()
                    .transactions(transactionsOfPage)
                    .debitCreditScore(calculateDebitCreditScore(transactionsOfPage))
                    .pageNo(currentPageNo)
                    .build();
            paginatedLists.add(paginatedList);

            beginning = end;
            end  = Math.min(end + pageSize, transactionAmount);
        }
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .pageSize(pageSize)
                .content(paginatedLists)
                .totalPages(requiredPages)
                .build();
        return transactionResponse;
    }

    public float calculateDebitCreditScore (List<Transaction> transactions){
        //According to API provider, base currency is USD
        String currencyRates = exchangeRateService.getCurrentExchangeRateBaseUSD();
        float debit_credit_score = 0;
        try{
            JsonNode responseJson = objectMapper.readTree(currencyRates);

            for (Transaction transaction : transactions) {
                String currentCurrency = String.valueOf(transaction.getCurrency());
                double currentAmount = transaction.getAmount();

                if (responseJson.get("base").asText() == "USD"){
                    debit_credit_score+=currentAmount;
                }

                JsonNode rates = responseJson.get("rates");
                String exchangeRateString = rates.get(currentCurrency).asText();
                double rate = Double.parseDouble(exchangeRateString);
                debit_credit_score+=currentAmount*rate;
            }
            //Limit 2 decimals
            return (debit_credit_score*100)/100;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);          //Always runtime exception
        }
    }

    public List<Transaction> consumeTransactionsFromTopic(String clientName, int year, int month){
        //Control 1: Check year and month is valid
        if (!requestControlService.validateYearAndMonth(year, month)) {
            throw new IllegalArgumentException("Review your request, the values of month and date may not be correct");
        }
        //Control 2: Check client is valid
        if (clientService.findClientByName(clientName).isEmpty()) {
            throw new IllegalArgumentException("Review your request, clientName: "+clientName+" is not present in our Data Base");
        }
        //Subscribe to topic
        String kafkaTopic = "transactions-" + year + "-" + clientName;
        //Calendar sets January to 0. It is necessary to substract 1
        int kafkaPartition = month-1;
        TopicPartition partition = new TopicPartition(kafkaTopic, kafkaPartition);
        kafkaConsumer.assign(List.of(partition));

        //Obtain data
        List<Transaction> transactionList = new ArrayList<>();

        //Poll for records
        //Reposition consumer to the beginning (0) of the 'partition'
        kafkaConsumer.seek(partition, 0);
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        do{
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                try {
                    Transaction transaction = objectMapper.readValue(consumerRecord.value(), Transaction.class);
                    transactionList.add(transaction);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error processing transaction record.", e);
                }
            }
            //Poll for more records
            consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        }
        while (!consumerRecords.isEmpty());
        //Unsubscribe consumer but no close
        kafkaConsumer.unsubscribe();
        return transactionList;
        }
    }

