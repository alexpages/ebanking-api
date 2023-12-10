package com.alexpages.ebankingapi.models;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.repository.ClientRepository;

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
        ClientEntity testClient = generateTestClient();

        // When
        ClientEntity savedClient = underTest.save(testClient);

        // Then
        assertEquals(savedClient.getName(), testClient.getName());
    }


    @Test
    void itShouldFindClientByName() {
        // Given
        ClientEntity testClient = generateTestClient();
        underTest.save(testClient);

        // When
        Optional<ClientEntity> foundClient = underTest.findClientByName(testClient.getName());

        // Then
        assertNotNull(foundClient);
    }

    @Test
    void itShouldNotFindClientByName() {
        // Given
        ClientEntity testClient = generateTestClient();

        // When
        Optional<ClientEntity> foundClient = underTest.findClientByName(testClient.getName());

        // Then
        assertThat(foundClient).isEmpty();
    }

    @Test
    void itShouldFindClientByAccount(){
        // Given
        ClientEntity testClient = generateTestClient();
        underTest.save(testClient);

        // When
        ClientEntity foundClient = underTest.findClientByAccount("iban");

        // Then
        assertThat(foundClient).isNotNull();
    }

    @Test
    void itShouldNotFindClientByAccount(){
        // Given
        ClientEntity testClient = generateTestClient();

        // When
        ClientEntity foundClient = underTest.findClientByAccount("iban");

        // Then
        assertThat(foundClient).isNull();
    }



    private ClientEntity generateTestClient() {
        ClientEntity testClient = ClientEntity.builder()
                .clientRole(ClientRole.USER)
                .id(1)
                .name("test")
                .password("test")
                .build();
        AccountEntity testAccount = AccountEntity.builder()
                .id(1)
                .iban("iban")
                .currency("EUR")
                .client(testClient)
                .build();
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(testAccount);
        testClient.setAccounts(accounts);
        return testClient;
    }

}