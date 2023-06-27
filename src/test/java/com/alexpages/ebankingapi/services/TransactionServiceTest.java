package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.models.account.Account;
import com.alexpages.ebankingapi.models.client.Client;
import com.alexpages.ebankingapi.models.client.ClientRole;
import com.alexpages.ebankingapi.models.transaction.Currency;
import com.alexpages.ebankingapi.models.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock private KafkaTemplate<String, String> kafkaTemplate;
    @Mock private Consumer<String, String> kafkaConsumer;
    @Mock private ClientService clientService;
    @Mock private Calendar calendar;
    @Mock private ObjectMapper objectMapper;
    @Mock private Client testClient;

    @InjectMocks
    private TransactionService underTest;

    @BeforeEach
    public void setup() {
        testClient = generateTestClient();
    }

    @Test
    public void itShouldPublishTransaction() throws JsonProcessingException {
        // Given
        Transaction transaction = Transaction.builder()
                .date(new Date())
                .iban("iban")
                .currency(Currency.EUR)
                .id("transactionId")
                .amount(100)
                .build();

        String messageKey = transaction.getId();
        String messageValue = objectMapper.writeValueAsString(transaction);

        // When
        when(clientService.findClientByAccount("iban")).thenReturn(testClient);
        when(objectMapper.writeValueAsString(transaction)).thenReturn(messageValue);
        when(kafkaTemplate.send(anyString(), anyInt(), eq(messageKey), eq(messageValue))).thenReturn(null);

        // Then
        Transaction sentTransaction = underTest.publishTransactionToTopic(transaction);
        assertEquals(sentTransaction, transaction);

        verify(clientService).findClientByAccount("iban");
        verify(objectMapper).writeValueAsString(transaction);
        verify(kafkaTemplate).send(anyString(), anyInt(), eq(messageKey), eq(messageValue));
    }

    private Client generateTestClient() {
        Client testClient = Client.builder()
                .clientRole(ClientRole.USER)
                .id(1)
                .name("testClient")
                .password("test")
                .build();
        return testClient;
    }

    @Test
    void getPaginatedTransactionListByUserAndDate() {
    }

    @Test
    void calculateDebitCreditScore() {
    }

    @Test
    void consumeTransactionsFromTopic() {
    }
}