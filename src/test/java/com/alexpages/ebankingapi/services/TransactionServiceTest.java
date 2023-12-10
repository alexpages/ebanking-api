package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.entity.TransactionEntity;
import com.alexpages.ebankingapi.others.Currency;
import com.alexpages.ebankingapi.repository.ClientRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    @Mock private ClientService clientService;
    @Mock private ClientRepository clientRepository;

    @InjectMocks
    private TransactionService underTest;

    @Test
    void itShouldPublishTransaction(){
        // Given
        TransactionEntity transaction = TransactionEntity.builder()
                .date(new Date())
                .iban("iban")
                .currency(Currency.EUR)
                .id("transactionId")
                .amount(100)
                .build();

        // When
        TransactionEntity sentTransaction = underTest.publishTransactionToTopic(transaction);

        // Then
        assertEquals(sentTransaction, transaction);
    }

}