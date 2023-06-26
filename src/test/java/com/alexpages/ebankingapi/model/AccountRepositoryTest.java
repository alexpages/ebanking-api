package com.alexpages.ebankingapi.model;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.account.AccountRepository;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.ClientRole;
import com.alexpages.ebankingapi.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;

    @Mock
    private ClientService clientService;

    @Test
    void itShouldFindAccountsByClient() {
        // Given
        Client testClient = generateTestClient();

        // When
        when(clientService.addClient(testClient)).thenReturn(testClient);
        clientService.addClient(testClient);

        List<Account> accounts = underTest.findAccountsByClient(testClient);

        // Then
        assertThat(accounts).isNotNull();
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