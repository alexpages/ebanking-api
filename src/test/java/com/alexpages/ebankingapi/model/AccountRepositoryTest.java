package com.alexpages.ebankingapi.model;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.account.AccountRepository;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void itShouldFindAccountsByClient() throws JsonProcessingException {
        // GIVEN
        String json = "{\"name\":\"synpulse8\",\"password\":\"password\",\"accounts\":[{\"iban\":\"CH93-0000-0000-0000-0000-0\",\"currency\":\"GBP\"},{\"iban\":\"EU93-0000-0000-0000-0000-0\",\"currency\":\"EUR\"}]}";
        Client client = new ObjectMapper().readValue(json, Client.class);
        Client builtClient = Client.builder()
                .password(client.getPassword())
                .name(client.getName())
                .accounts(client.getAccounts())
                .role(Role.USER)
                .build();
        Client savedClient = clientRepository.save(builtClient);

        // WHEN
        List<Account> accounts = underTest.findAccountsByClient(savedClient);

        // THEN
        assertThat(accounts).isNotNull();
    }

    @Test
    void findClientByAccount() throws JsonProcessingException {
        // GIVEN
        String iban = "CH93-0000-0000-0000-0000-0";
        String json = "{\"name\":\"synpulse8\",\"password\":\"password\",\"accounts\":[{\"iban\":\"CH93-0000-0000-0000-0000-0\",\"currency\":\"GBP\"},{\"iban\":\"EU93-0000-0000-0000-0000-0\",\"currency\":\"EUR\"}]}";
        Client client = new ObjectMapper().readValue(json, Client.class);
        Client builtClient = Client.builder()
                .password(client.getPassword())
                .name(client.getName())
                .accounts(client.getAccounts())
                .role(Role.USER)
                .build();
        Client savedClient = clientRepository.save(builtClient);

        // WHEN
        Client foundClient = underTest.findClientByAccount(iban);

        // THEN
        assertThat(foundClient).isEqualTo(savedClient);
    }
}