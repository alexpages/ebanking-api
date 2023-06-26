package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {


    @Test
    void extractUsername() {
    }

    @Test
    void extractClaim() {
    }

    @Test
    void generateToken() {
    }

    @Test
    void testGenerateToken() {
    }

    @Test
    void isTokenValid() {
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