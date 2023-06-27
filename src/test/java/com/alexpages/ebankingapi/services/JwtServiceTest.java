package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.models.client.Client;
import com.alexpages.ebankingapi.models.client.ClientRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(classes = JwtService.class)
class JwtServiceTest {

    @Autowired
    private JwtService underTest;
    private Client client;

    @BeforeEach
    void setUp(){
        client = generateTestClient();
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
                .id(anyInt())
                .name(anyString())
                .password(anyString())
                .build();
        return testClient;
    }

}