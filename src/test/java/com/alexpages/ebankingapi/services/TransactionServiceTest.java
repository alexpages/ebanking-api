package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Transaction;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.repository.ClientRepository;
import com.alexpages.ebankingapi.service.ClientService;
import com.alexpages.ebankingapi.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Calendar;
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

	private EasyRandom easyRandom;
	
    @BeforeEach
    public void setUp() {
        easyRandom = new EasyRandom();
    }

    @Test
    void it_Should_Publish_Transaction() throws JsonProcessingException 
    {
        when(clientService.findClientByAccount(any(String.class))).thenReturn(Optional.of(easyRandom.nextObject(ClientEntity.class)));
        when(objectMapper.writeValueAsString(any())).thenReturn("validString");
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(easyRandom.nextObject(CompletableFuture.class));   
        Transaction sentTransaction = transactionService.publishTransactionToTopic(easyRandom.nextObject(Transaction.class));
        assertNotNull(sentTransaction);
    }
}