package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.service.JwtService;
import org.jeasy.random.EasyRandom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private EasyRandom easyRandom = new EasyRandom();

    @Test
    void itShouldGenerateCorrectTokenAndRelateToTheClient() 
    throws Exception 
    {
        UserDetails userDetails = easyRandom.nextObject(UserDetails.class);
        String jwtToken = jwtService.generateToken(userDetails);
        String expectedName = jwtService.extractUsername(jwtToken);
        assertNotNull(jwtToken);
        assertTrue(jwtService.isTokenValid(jwtToken, userDetails));
        assertEquals(expectedName, userDetails.getUsername());
    }

    @Test
    void tokenShouldBeValid() 
    {
        UserDetails userDetails = easyRandom.nextObject(UserDetails.class);
        String jwtToken = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(jwtToken, userDetails));
    }
}
