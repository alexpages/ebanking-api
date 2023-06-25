package com.alexpages.ebankingapi.model;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.ClientRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(connection =  EmbeddedDatabaseConnection.H2)
class ClientRepositoryTest {

    @Mock
    private ClientRepository underTest;

    @BeforeEach
    void setup() {
        underTest.deleteAll();
    }


    @Test
    void itShouldAddClient(){
        // Given
        Client testClient = generateTestClient();

        // When
        when(underTest.save(testClient)).thenReturn(testClient);
        Client expectedClient = underTest.save(testClient);

        // Then
        assertEquals(testClient, expectedClient);
    }


    @Test
    void itShouldFindClientByName() {
        // Given
        Client testClient = generateTestClient();
        underTest.save(testClient);

        // When
        when(underTest.findClientByName("test")).thenReturn(Optional.of(testClient));
        Optional<Client> foundClient = underTest.findClientByName(testClient.getName());

        // Then
        assertEquals(Optional.of(testClient), foundClient);
    }

    @Test
    void itShouldNotFindClientByName() {
        // Given
        Client testClient = generateTestClient();

        // When
        when(underTest.findClientByName("test")).thenReturn(null);
        Optional<Client> foundClient = underTest.findClientByName(testClient.getName());

        // Then
        assertNull(foundClient);
    }

    @Test
    void itShouldFindClientByAccount(){
        // Given
        Client testClient = generateTestClient();
        underTest.save(testClient);

        // When
        when(underTest.findClientByAccount("iban")).thenReturn(testClient);
        Client foundClient = underTest.findClientByAccount("iban");

        // Then
        assertEquals(foundClient, testClient);
    }

    @Test
    void itShouldNotFindClientByAccount(){
        // Given
        Client testClient = generateTestClient();
        underTest.save(testClient);

        // When
        when(underTest.findClientByAccount("iban2")).thenReturn(null);
        Client foundClient = underTest.findClientByAccount("iban");

        // Then
        assertNull(foundClient);
    }


    private Client generateTestClient() {
        Client testClient = Client.builder()
                .clientRole(ClientRole.USER)
                .id(1)
                .name("test")
                .password("test")
                .build();
        Account testAccount = Account.builder()
                .id(1)
                .iban("iban")
                .currency("EUR")
                .client(testClient)
                .build();
        List<Account> accounts = new ArrayList<>();
        accounts.add(testAccount);
        testClient.setAccounts(accounts);
        return testClient;
    }

}