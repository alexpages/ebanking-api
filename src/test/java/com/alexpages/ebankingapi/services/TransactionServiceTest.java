package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.domain.InputDataSearch;
import com.alexpages.ebankingapi.domain.Transaction;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.repository.ClientRepository;
import com.alexpages.ebankingapi.service.ClientService;
import com.alexpages.ebankingapi.service.TransactionService;
import com.alexpages.ebankingapi.service.ValidateDataService;
import com.alexpages.ebankingapi.utils.PageableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;
    
    @Mock 
    private ClientService clientService;   
    @Mock
    private Calendar calendar;   
    @Mock 
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock 
    private ClientRepository clientRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private Consumer<String, String> kafkaConsumer;

	private EasyRandom easyRandom;	
	
    @BeforeEach
    public void setUp() {
        easyRandom = new EasyRandom();
    }

    @Test
    void it_Should_Throw_Exception_When_Client_NotFound() throws JsonProcessingException 
    {
        when(clientService.findClientByAccount(any(String.class))).thenReturn(Optional.empty());
        assertThrows(EbankingManagerException.class, () -> {
            transactionService.publishTransactionToTopic(easyRandom.nextObject(Transaction.class));
        });
    }

    @Test
    void it_Should_SendTransaction_To_Kafka_Successfully() throws JsonProcessingException 
    {
        when(clientService.findClientByAccount(any(String.class))).thenReturn(Optional.of(easyRandom.nextObject(ClientEntity.class)));
        when(kafkaTemplate.send(any(), any(), any(), any())).thenReturn(new CompletableFuture<>());
        Transaction sentTransaction = transactionService.publishTransactionToTopic(easyRandom.nextObject(Transaction.class));
        assertNotNull(sentTransaction);
    }
    
//    @Test
//    void it_Should_Consume_Transactions_From_Topic_Successfully() throws JsonProcessingException 
//    {
//        when(clientService.findClientByAccount(any(String.class)))
//                .thenReturn(Optional.of(easyRandom.nextObject(ClientEntity.class)));
//        ConsumerRecord<String, String> mockConsumerRecord = new ConsumerRecord<>("topic", 0, 0, "key", "value");
//        List<ConsumerRecord<String, String>> records = Collections.singletonList(mockConsumerRecord);
//        when(kafkaConsumer.poll(any())).thenReturn(records);
//        when(objectMapper.readValue(any(String.class), Transaction.class)).thenReturn(easyRandom.nextObject(Transaction.class));
//        when(kafkaTemplate.send(any(), any(), any(), any())).thenReturn(new CompletableFuture<>());
//        Transaction sentTransaction = transactionService.publishTransactionToTopic(easyRandom.nextObject(Transaction.class));
//        assertNotNull(sentTransaction);
//        verify(clientService).findClientByAccount(any(String.class));
//        verify(kafkaConsumer).poll(any());
//        verify(objectMapper).readValue(anyString(), eq(Transaction.class));
//        verify(kafkaTemplate).send(any(), any(), any(), any());
//    }
 }