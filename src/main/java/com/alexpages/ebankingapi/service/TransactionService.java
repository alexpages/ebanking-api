package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    //Acts as a publisher and a subscriber(consumer)
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Calendar calendar = Calendar.getInstance();
    private final ClientDataService clientDataService;
    private final Consumer<String, String> kafkaConsumer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RequestControlService requestControlService;

    //PUBLISHER
    public Transaction publishTransactionToTopic(Transaction transaction) throws JsonProcessingException {
        //Schema: topic = year+client, partition = month -> 12 topics per client
        String clientName = clientDataService
                .getClientByAccount(transaction.getIban())
                .getUsername();

        //Get transaction date and obtain topic to be published
        calendar.setTime(transaction.getDate());
        int transactionPartitionMonth = calendar.get(Calendar.MONTH)-1;                  //will be the partition of the topic
        int transactionYear = calendar.get(Calendar.YEAR);                              //will help define the topic
        String transactionTopic = "transactions-" + transactionYear + "-" + clientName; //topic

        try {
            String messageKey = transaction.getId();
            String messageValue = objectMapper.writeValueAsString(transaction); //Throws JsonProcessingException
            //Send transaction to topic
//            TopicBuilder.name(transactionTopic).partitions(12).build();

            kafkaTemplate.send(transactionTopic, transactionPartitionMonth, messageKey, messageValue);
            return transaction;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);          //Always runtime exception
        }
    }

    //CONSUMER
    public List<Transaction> consumeTransactionsFromTopic(String clientName, int year, int month){
        //Control 1: Check year and month is valid
        if (!requestControlService.validateYearAndMonth(year, month)) {
            throw new IllegalArgumentException("Review your request, the values of month and date may not be correct");
        }
        // Control 2: Check client is valid
        if (!requestControlService.clientIsPresent(clientName)) {
            throw new IllegalArgumentException("Review your request, clientName: "+clientName+" is not present in our Data Base");
        }
        //Subscribe to topic
        String kafkaTopic = "transactions-" + year + "-" + clientName;
        int kafkaPartition = month-1;
        TopicPartition partition = new TopicPartition(kafkaTopic, kafkaPartition);
        kafkaConsumer.assign(List.of(partition));

        //Obtain data
        List<Transaction> transactionList = new ArrayList<>();

        // Poll for records
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(1000));
        kafkaConsumer.seek(partition, 0);
        while(!consumerRecords.isEmpty()){
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

        // Close the consumer
        kafkaConsumer.close();
        return transactionList;
        }
    }

