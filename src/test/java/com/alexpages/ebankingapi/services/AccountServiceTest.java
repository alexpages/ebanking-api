package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.models.account.Account;
import com.alexpages.ebankingapi.models.account.AccountRepository;
import com.alexpages.ebankingapi.models.client.Client;
import com.alexpages.ebankingapi.models.client.ClientRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock private AccountRepository accountRepository;
    @Mock private ClientService clientService;

    @InjectMocks
    private AccountService underTest;


    @Test
    void itShouldFindAccountsByClient() {
        // Given
        Client testClient = generateTestClient();
        clientService.addClient(testClient);
        // When
        when(underTest.findAccountsByClient(testClient)).thenReturn(testClient.getAccounts());
        List<Account> foundAccounts = underTest.findAccountsByClient(testClient);

        // Then
        verify(accountRepository).findAccountsByClient(testClient);
        assertEquals(testClient.getAccounts(), foundAccounts);
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