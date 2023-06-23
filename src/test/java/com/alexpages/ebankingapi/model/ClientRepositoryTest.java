package com.alexpages.ebankingapi.model;

import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection =  EmbeddedDatabaseConnection.H2)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository underTest;

    @Test
    void itShouldFindClientByName() throws JsonProcessingException {
        // GIVEN
        String json = "{\"name\":\"synpulse8\",\"password\":\"password\",\"accounts\":[{\"iban\":\"CH93-0000-0000-0000-0000-0\",\"currency\":\"GBP\"},{\"iban\":\"EU93-0000-0000-0000-0000-0\",\"currency\":\"EUR\"}]}";
        Client client = new ObjectMapper().readValue(json, Client.class);
        Client builtClient = Client.builder()
                .password(client.getPassword())
                .name(client.getName())
                .accounts(client.getAccounts())
                .role(Role.USER)
                .build();
        Client savedClient = underTest.save(builtClient);

        // WHEN
        Optional<Client> foundClient = underTest.findClientByName(savedClient.getName());

        // THEN
        assertThat(foundClient.isPresent()).isTrue();
    }

    @Test
    void itShouldNotFindClientByName() throws JsonProcessingException {
        // GIVEN
        String json = "{\"name\":\"synpulse8\",\"password\":\"password\",\"accounts\":[{\"iban\":\"CH93-0000-0000-0000-0000-0\",\"currency\":\"GBP\"},{\"iban\":\"EU93-0000-0000-0000-0000-0\",\"currency\":\"EUR\"}]}";
        Client client = new ObjectMapper().readValue(json, Client.class);
        Client builtClient = Client.builder()
                .password(client.getPassword())
                .name(client.getName())
                .accounts(client.getAccounts())
                .role(Role.USER)
                .build();

        // Save the builtClient to the repository (assuming underTest is a repository or service)
        underTest.save(builtClient);
        // WHEN
        Optional<Client> foundClient = underTest.findClientByName("clientThatDoesNotExist");

        // THEN
        assertThat(foundClient.isPresent()).isFalse();
    }

}