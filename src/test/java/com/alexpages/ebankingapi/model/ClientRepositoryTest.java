package com.alexpages.ebankingapi.model;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.ClientRole;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    void itShouldFindClientByName() throws JsonProcessingException {
        // GIVEN
        Client testClient = generateTestClient();

        // WHEN
        underTest.save(testClient);
        Optional<Client> foundClient = underTest.findClientByName(testClient.getName());

        // THEN
        assertThat(foundClient.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindClientByName() {
        // GIVEN
        Client testClient = generateTestClient();

        // WHEN
        underTest.save(testClient);
        Optional<Client> foundClient = underTest.findClientByName("clientWithIncorrectName");

        // THEN
        assertThat(foundClient.isPresent()).isFalse();
    }

    @Test
    void itShouldFindClientByAccount(){
        // GIVEN
        Client testClient = generateTestClient();

        // WHEN
        underTest.save(testClient);
        Client foundClient = underTest.findClientByAccount("iban");

        // THEN
        assertThat(foundClient).isNotNull();
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