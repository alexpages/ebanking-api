package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.entity.ClientEntity;
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
    void it_Should_Generate_Token_Correctly() 
    throws Exception 
    {
        ClientEntity clientEntity = easyRandom.nextObject(ClientEntity.class);
        String jwtToken = jwtService.generateToken(clientEntity);
        String expectedName = jwtService.extractUsername(jwtToken);
        assertNotNull(jwtToken);
        assertTrue(jwtService.isTokenValid(jwtToken, clientEntity));
        assertEquals(expectedName, clientEntity.getUsername());
    }
    
    @Test
    void it_Should_Validate_Token_Correctly() 
    {
        UserDetails userDetails = easyRandom.nextObject(UserDetails.class);
        String jwtToken = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(jwtToken, userDetails));
    }
}
