package com.alexpages.ebankingapi.model;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.account.AccountRepository;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import com.alexpages.ebankingapi.model.client.ClientRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

    @Mock private AccountRepository underTest;

    @Mock private ClientRepository clientRepository;

    @Test
    void itShouldFindAccountsByClient() {
        // Given
        Client testClient = generateTestClient();
        clientRepository.save(testClient);

        // When
        when(underTest.findAccountsByClient(testClient)).thenReturn(testClient.getAccounts());
        List<Account> accounts = underTest.findAccountsByClient(testClient);

        // Then
        assertEquals(accounts,testClient.getAccounts());
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