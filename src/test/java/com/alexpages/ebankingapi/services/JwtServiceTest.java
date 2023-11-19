package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.utils.ClientRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JwtService.class)
class JwtServiceTest {

    @Autowired
    private JwtService underTest;
    private Client client;

    @BeforeEach
    void setUp(){
        client = generateTestClient();
    }

    @AfterTestMethod
    public void tearDown() {
        client = null;
    }

    @Test
    void itShouldGenerateCorrectTokenAndRelateToTheClient() throws Exception{
        // Given

        // When
        String jwtToken = underTest.generateToken(client);
        String expectedName = underTest.extractUsername(jwtToken);
        boolean validityOfToken = underTest.isTokenValid(jwtToken, client);

        // Then
        assertNotNull(jwtToken);
        assertTrue(validityOfToken);
        assertEquals(expectedName, client.getName());
    }

    @Test
    void tokenShouldBeValid(){
        // Given

        // When
        String jwtToken = underTest.generateToken(client);
        boolean result = underTest.isTokenValid(jwtToken, client);

        // Then
        assertTrue(result);
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