package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.repository.AccountRepository;
import com.alexpages.ebankingapi.service.AccountService;
import com.alexpages.ebankingapi.service.ClientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock private AccountRepository accountRepository;
    @Mock private ClientService clientService;

    @InjectMocks
    private AccountService underTest;
    private ClientEntity testClient;

    @BeforeEach
    void setUp(){
        testClient = generateTestClient();
    }

    @Test
    void itShouldFindAccountsByClient() {
        // Given
        clientService.addClient(testClient);
        // When
        when(underTest.findAccountsByClient(testClient)).thenReturn(testClient.getAccounts());
        List<AccountEntity> foundAccounts = underTest.findAccountsByClient(testClient);

        // Then
        verify(accountRepository).findAccountsByClient(testClient);
        assertEquals(testClient.getAccounts(), foundAccounts);
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