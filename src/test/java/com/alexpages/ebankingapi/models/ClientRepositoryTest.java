package com.alexpages.ebankingapi.models;

import com.alexpages.ebankingapi.models.account.Account;
import com.alexpages.ebankingapi.models.client.Client;
import com.alexpages.ebankingapi.models.client.ClientRepository;
import com.alexpages.ebankingapi.models.client.ClientRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection =  EmbeddedDatabaseConnection.H2)
class ClientRepositoryTest {

    @Autowired
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
        Client savedClient = underTest.save(testClient);

        // Then
        assertEquals(savedClient.getName(), testClient.getName());
    }


    @Test
    void itShouldFindClientByName() {
        // Given
        Client testClient = generateTestClient();
        underTest.save(testClient);

        // When
        Optional<Client> foundClient = underTest.findClientByName(testClient.getName());

        // Then
        assertNotNull(foundClient);
    }

    @Test
    void itShouldNotFindClientByName() {
        // Given
        Client testClient = generateTestClient();

        // When
        Optional<Client> foundClient = underTest.findClientByName(testClient.getName());

        // Then
        assertThat(foundClient).isEmpty();
    }

    @Test
    void itShouldFindClientByAccount(){
        // Given
        Client testClient = generateTestClient();
        underTest.save(testClient);

        // When
        Client foundClient = underTest.findClientByAccount("iban");

        // Then
        assertThat(foundClient).isNotNull();
    }

    @Test
    void itShouldNotFindClientByAccount(){
        // Given
        Client testClient = generateTestClient();

        // When
        Client foundClient = underTest.findClientByAccount("iban");

        // Then
        assertThat(foundClient).isNull();
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