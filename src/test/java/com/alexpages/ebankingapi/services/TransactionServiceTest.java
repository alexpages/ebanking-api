package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.models.client.ClientRepository;
import com.alexpages.ebankingapi.models.transaction.Currency;
import com.alexpages.ebankingapi.models.transaction.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    @Mock private ClientService clientService;
    @Mock private ClientRepository clientRepository;

    @InjectMocks
    private TransactionService underTest;

    @Test
    public void itShouldPublishTransaction(){
        // Given
        Transaction transaction = Transaction.builder()
                .date(new Date())
                .iban("iban")
                .currency(Currency.EUR)
                .id("transactionId")
                .amount(100)
                .build();

        // When
        Transaction sentTransaction = underTest.publishTransactionToTopic(transaction);

        // Then
        assertEquals(sentTransaction, transaction);
    }

}