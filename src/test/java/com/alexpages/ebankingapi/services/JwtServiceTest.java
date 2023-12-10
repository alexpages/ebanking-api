package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.service.JwtService;

import org.junit.jupiter.api.AfterEach;
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
    private ClientEntity client;

    @BeforeEach
    void setUp(){
        client = generateTestClient();
    }

    @AfterEach
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