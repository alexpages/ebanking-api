package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Transaction;
import com.alexpages.ebankingapi.repository.ClientRepository;
import com.alexpages.ebankingapi.service.ClientService;
import com.alexpages.ebankingapi.service.TransactionService;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;
    
    @Mock 
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock 
    private ClientService clientService;
    @Mock 
    private ClientRepository clientRepository;

	private EasyRandom easyRandom;
	
	@BeforeEach
	public void setUp() {
		easyRandom = new EasyRandom();
	}

    @Test
    void it_Should_Publish_Transaction()
    {
        Transaction sentTransaction = transactionService.publishTransactionToTopic(easyRandom.nextObject(Transaction.class));
        assertNotNull(sentTransaction);
    }
}