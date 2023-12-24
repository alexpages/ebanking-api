package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.service.JwtService;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

	@InjectMocks
    private JwtService jwtService;

	private EasyRandom easyRandom;
	
	@BeforeEach
	public void setUp() {
		easyRandom = new EasyRandom();
	}

    @Test
    void itShouldGenerateCorrectTokenAndRelateToTheClient() 
    throws Exception
    {
        String jwtToken = jwtService.generateToken(client);
        String expectedName = jwtService.extractUsername(jwtToken);
        boolean validityOfToken = jwtService.isTokenValid(jwtToken, client);
        assertNotNull(jwtToken);
        assertTrue(validityOfToken);
        assertEquals(expectedName, client.getName());
    }

    @Test
    void tokenShouldBeValid()
    {
        String jwtToken = jwtService.generateToken(easyRandom.nextObject(UserDetails.class));
        assertTrue(jwtService.isTokenValid(jwtToken, client));
    }
}