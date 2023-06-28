package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.models.account.Account;
import com.alexpages.ebankingapi.models.client.Client;
import com.alexpages.ebankingapi.models.client.ClientRepository;
import com.alexpages.ebankingapi.models.client.ClientRole;
import com.alexpages.ebankingapi.models.transaction.Currency;
import com.alexpages.ebankingapi.models.transaction.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
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
    @Mock private ExchangeRateService exchangeRateService;
    @Mock private Calendar calendar;
    @Mock private ObjectMapper objectMapper;
    @Mock private ClientRepository clientRepository;

    @InjectMocks
    private TransactionService underTest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
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

        // When
        Transaction sentTransaction = underTest.publishTransactionToTopic(transaction);

        // Then
        assertEquals(sentTransaction, transaction);
    }

//    @Test
//    void itShouldCalculateDebitCreditScore() throws JsonProcessingException {
//        // Given
//        Transaction transaction = Transaction.builder()
//                .date(new Date())
//                .iban("iban")
//                .currency(Currency.EUR)
//                .id("transactionId")
//                .amount(100)
//                .build();
//        List<Transaction> transactionList= new ArrayList<>();
//        transactionList.add(transaction);
//
//        // Example
//        String jsonString = "{\n" +
//                "  \"date\": \"2023-03-21 12:43:00+00\",\n" +
//                "  \"base\": \"USD\",\n" +
//                "  \"rates\": {\n" +
//                "    \"EUR\": \"0.85\"\n" +
//                "  }\n" +
//                "}";
//        JsonNode jsonNode = objectMapper.readTree(jsonString);
//        // When
//        when(exchangeRateService.getCurrentExchangeRateBaseUSD()).thenReturn(jsonString);
//        when(objectMapper.readTree(anyString())).thenReturn(jsonNode);
//        float calculatedDebitCredit = underTest.calculateDebitCreditScore(transactionList);
//
//        // Then
//        assertNotNull(calculatedDebitCredit);
//    }

//    @Test
//    public void itShouldConsumeTransactionsFromTopic() throws JsonProcessingException {
//        // Given
//        int year = 2020;
//        int month = 10;
//        Client testClient = generateTestClient();
//        // When
//        when(clientService.findClientByName(anyString())).thenReturn(Optional.ofNullable(testClient));
//        Transaction transaction = Transaction.builder()
//                .date(new Date())
//                .iban("iban")
//                .currency(Currency.EUR)
//                .id("transactionId")
//                .amount(100)
//                .build();
//
//        // When
//        Transaction sentTransaction = underTest.publishTransactionToTopic(transaction);
//
//        // Then
//        assertEquals(sentTransaction, transaction);
//    }
//
//
//    private Client generateTestClient() {
//        Client testClient = Client.builder()
//                .clientRole(ClientRole.USER)
//                .id(1)
//                .name("testClient")
//                .password("test")
//                .build();
//        return testClient;
//    }
//
//    @Test
//    void getPaginatedTransactionListByUserAndDate() {
//    }
//
//
//    @Test
//    void consumeTransactionsFromTopic() {
//    }
}