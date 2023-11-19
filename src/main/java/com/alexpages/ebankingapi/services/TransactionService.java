package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Transaction;
import com.alexpages.ebankingapi.exceptions.EbankingManagerException;
import com.alexpages.ebankingapi.utils.PaginatedList;
import com.alexpages.ebankingapi.utils.TransactionControllerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
	
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	private KafkaTemplate<String, String> kafkaTemplate;
    private ClientService clientService;
    private ValidateDataService validateDataService;
    private ExchangeRateService exchangeRateService; 
    private Calendar calendar = Calendar.getInstance();
    private Consumer<String, String> kafkaConsumer;
    private ObjectMapper objectMapper = new ObjectMapper();
	
    public TransactionService(KafkaTemplate<String, String> kafkaTemplate, ClientService clientService,
			ValidateDataService validateDataService, ExchangeRateService exchangeRateService,
			Consumer<String, String> kafkaConsumer, ObjectMapper objectMapper) 
    {
		this.kafkaTemplate = kafkaTemplate;
		this.clientService = clientService;
		this.validateDataService = validateDataService;
		this.exchangeRateService = exchangeRateService;
		this.kafkaConsumer = kafkaConsumer;
		this.objectMapper = objectMapper;
	}
    
    public Transaction publishTransactionToTopic(Transaction transaction)
    {
        String clientName = clientService.findClientNameByAccount(transaction.getIban());

        calendar.setTime(transaction.getDate());
        int transactionPartitionMonth = calendar.get(Calendar.MONTH);                   //will be the partition of the topic
        int transactionYear = calendar.get(Calendar.YEAR);                              //will help define the topic
        String transactionTopic = "transactions-" + transactionYear + "-" + clientName; //topic

        try {
            String messageKey = transaction.getId();
            String messageValue = objectMapper.writeValueAsString(transaction);
            // Send transaction to topic
            kafkaTemplate.send(transactionTopic, transactionPartitionMonth, messageKey, messageValue);
            // Log
            logger.info("Transaction {} has been published", transaction);
            return transaction;
        } catch (JsonProcessingException e) {
            // Log
            logger.error("Transaction {} could not be published", transaction);
            throw new EbankingManagerException("Transaction {} could not be published");          //Always runtime exception
        }
    }

    // CONSUMER

    public TransactionControllerResponse getPaginatedTransactionListByUserAndDate(int pageSize, String clientName, int year, int month){
        List<Transaction> transactionList = consumeTransactionsFromTopic(clientName,year,month);
        int transactionAmount = transactionList.size();
        int requiredPages = (int) Math.ceil((double) transactionAmount / pageSize);

        List<PaginatedList> paginatedLists = new ArrayList<>();
        int beginning = 0;
        int end = pageSize;

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
        TransactionControllerResponse transactionControllerResponse = TransactionControllerResponse.builder()
                .pageSize(pageSize)
                .content(paginatedLists)
                .totalPages(requiredPages)
                .build();
        logger.info("Paginated transactions list has been obtained");
        return transactionControllerResponse;
    }

    public float calculateDebitCreditScore (List<Transaction> transactionsList){
    	
        String currencyRates = exchangeRateService.getCurrentExchangeRateBaseUSD();
        float debit_credit_score = 0;
        try{
            JsonNode responseJson = objectMapper.readTree(currencyRates);
            for (Transaction transaction : transactionsList) {
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
            // Limit decimals
            return (debit_credit_score*100)/100;

        } catch (JsonProcessingException e) {
            // Log
            logger.error("Failed to calculate the Debit/Credit score");
            logger.debug("Failed to map Json to currency rates");
            throw new RuntimeException(e);
        }
    }

    public List<Transaction> consumeTransactionsFromTopic(String clientName, int year, int month){
        if (!validateDataService.validateYear(year)) {
            // Log
            logger.error("Year value {} from request is invalid", year);
            throw new IllegalArgumentException();
        }
        if (!validateDataService.validateMonth(month)) {
            // Log
            logger.error("The month value {} from request is invalid", month);
            throw new IllegalArgumentException();
        }
        if (clientService.findClientByName(clientName).isEmpty()) {
            // Log
            logger.error("Client by username {} is not found in the DB", clientName);
            throw new IllegalArgumentException();
        }
        //Subscribe to topic
        String kafkaTopic = "transactions-" + year + "-" + clientName;
        //Calendar sets January to 0. It is necessary to substract 1
        int kafkaPartition = month-1;
        TopicPartition partition = new TopicPartition(kafkaTopic, kafkaPartition);
        kafkaConsumer.assign(List.of(partition));

        //Obtain data
        List<Transaction> transactionsList = new ArrayList<>();

        //Poll for records starting from beginning (0) of the partition
        kafkaConsumer.seek(partition, 0);
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        do{
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                try {
                    Transaction transaction = objectMapper.readValue(consumerRecord.value(), Transaction.class);
                    transactionsList.add(transaction);
                } catch (JsonProcessingException e) {
                    // Log
                    logger.error("Kafka Consumer failed to read Kafka from topic");
                    logger.debug("Failed to map Json to Transaction");
                    throw new RuntimeException(e);
                }
            }
            //Poll for more records
            consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        }
        while (!consumerRecords.isEmpty());
        //Unsubscribe consumer but no close
        kafkaConsumer.unsubscribe();
        return transactionsList;
        }
    }

