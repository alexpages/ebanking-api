package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.domain.InputDataSearch;
import com.alexpages.ebankingapi.domain.Transaction;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.utils.DateUtils;
import com.alexpages.ebankingapi.utils.PageableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class TransactionService {

	private final KafkaTemplate<String, String> kafkaTemplate;
    private final ClientService clientService;
    private final ValidateDataService validateDataService;
    private final ExchangeRateService exchangeRateService; 
    private final Calendar calendar = Calendar.getInstance();
    private final Consumer<String, String> kafkaConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Transaction publishTransactionToTopic(Transaction transaction)
    {   
    	Optional<ClientEntity> clientEntity = clientService.findClientByAccount(transaction.getIban());
    	if (!clientEntity.isPresent())
    	{
    		throw new EbankingManagerException("Client does not exist"); 
    	}
    	String clientName = clientEntity.get().getName();
        calendar.setTime(DateUtils.localDateToDate(transaction.getDate()));
        int transactionPartitionMonth = calendar.get(Calendar.MONTH);                   
        int transactionYear = calendar.get(Calendar.YEAR);                              
        String transactionTopic = "transactions-" + transactionYear + "-" + clientName; 
        try 
        {
            String messageKey = transaction.getIban();
            String messageValue = objectMapper.writeValueAsString(transaction.toString());
            kafkaTemplate.send(transactionTopic, transactionPartitionMonth, messageKey, messageValue);
            log.info("Transaction {} has been published", transaction);
            return transaction;
        } 
        catch (JsonProcessingException e) 
        {
            log.error("Transaction {} could not be published", transaction);
            throw new EbankingManagerException("Transaction could not be published");         
        }
    }

    public Page<Transaction> getTransactions(InputDataSearch inputDataSearch)
    {
    	try 
    	{
    		Pageable pageable = PageableUtils.getPageable(inputDataSearch.getPaginationBody());	
            List<Transaction> transactionList = consumeTransactionsFromTopic(inputDataSearch.getClientName(),inputDataSearch.getYear(),inputDataSearch.getMonth());
            return new PageImpl<Transaction>(transactionList, pageable, transactionList.size());    
    	}
    	catch (Exception e)
    	{
    		throw new EbankingManagerException("It was not possible to retrieve the transaction list");     
    	}
    }

    private List<Transaction> consumeTransactionsFromTopic(String clientName, int year, int month)
    {
    	if (!validateDates(year, month))
    	{
    		throw new EbankingManagerException("Date format is not valid");     
    	}		
        if (!clientService.findClientByName(clientName).isPresent()) 
        {
        	throw new EbankingManagerException("Client name is not present in DB"); 
        }
        String kafkaTopic = "transactions-" + year + "-" + clientName;
        int kafkaPartition = month-1;
        TopicPartition partition = new TopicPartition(kafkaTopic, kafkaPartition);
        
        kafkaConsumer.assign(Arrays.asList(partition));
        List<Transaction> transactionsList = new ArrayList<>();
        kafkaConsumer.seek(partition, 0);
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        do
        {
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) 
            {
                try 
                {
                	Transaction transaction = objectMapper.readValue(consumerRecord.value(), Transaction.class);
                    transactionsList.add(transaction);
                } 
                catch (JsonProcessingException e) 
                {
                	log.error("Kafka Consumer failed to read Kafka from topic");
                	throw new EbankingManagerException("Kafka Consumer failed to read Kafka from topic");  
                }
            }
            consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        }
        while (!consumerRecords.isEmpty());
        
        kafkaConsumer.unsubscribe();
        return transactionsList;
    }
    
    public float calculateDebitCreditScore (List<Transaction> transactionsList)
    {
        String currencyRates = exchangeRateService.getCurrentExchangeRateBaseUSD();
        float score = 0;
        try
        {
            JsonNode responseJson = objectMapper.readTree(currencyRates);
            for (Transaction transaction : transactionsList) 
            {
                String currentCurrency = String.valueOf(transaction.getMonetaryAmount().getCurrency());
                double currentAmount = transaction.getMonetaryAmount().getValue();

                if (responseJson.get("base").asText() == "USD")
                {
                	score+=currentAmount;
                }
                JsonNode rates = responseJson.get("rates");
                String exchangeRateString = rates.get(currentCurrency).asText();
                double rate = Double.parseDouble(exchangeRateString);
                score+=currentAmount*rate;
            }
            return (score*100)/100;
        } 
        catch (JsonProcessingException e) 
        {
        	log.error("Failed to calculate the Debit/Credit score");
        	log.debug("Failed to map Json to currency rates");
            throw new RuntimeException(e);
        }
    }
    
    private Boolean validateDates(int year, int month)
    {
        if (!validateDataService.validateYear(year)) {
        	log.error("Year value {} from request is invalid", year);
            throw new IllegalArgumentException();
        }
        if (!validateDataService.validateMonth(month)) {
        	log.error("The month value {} from request is invalid", month);
            throw new IllegalArgumentException();
        }
        return true;	
    }
}