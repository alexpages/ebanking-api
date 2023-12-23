package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.AuthenticateRequest;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.domain.Client.RoleEnum;
import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.mapper.EbankingMapper;
import com.alexpages.ebankingapi.others.AuthenticationResponse;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.repository.ClientRepository;
import com.alexpages.ebankingapi.service.ClientService;
import com.alexpages.ebankingapi.service.JwtService;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;  	
	@Mock 
	private ClientRepository clientRepository;
	@Mock 
	private PasswordEncoder passwordEncoder;
	@Mock 
	private EbankingMapper ebankingMapper;
	@Mock
	private JwtService jwtService;	
	@Mock
	private AuthenticationManager authenticationManager;
	
	private EasyRandom easyRandom;
    	
	@BeforeEach
	public void setUp() {
		easyRandom = new EasyRandom();
	}

    @Test
    void it_Should_Add_Client() 
    {
        ClientEntity mockClientEntity = easyRandom.nextObject(ClientEntity.class);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(ebankingMapper.toClientEntity(any(Client.class))).thenReturn(mockClientEntity);
        when(clientRepository.saveAndFlush(any())).thenReturn(mockClientEntity);
        ClientEntity result = clientService.addClient(easyRandom.nextObject(Client.class));
        assertNotNull(result);
    }
    
    @Test
    void it_Should_Not_Add_Client() 
    {
        when(passwordEncoder.encode(any(String.class))).thenThrow(new RuntimeException("some error"));
        assertThrows(EbankingManagerException.class, () -> clientService.addClient(easyRandom.nextObject(Client.class)));
    }

    @Test
    void it_Should_Generate_Token() 
    {
        when(jwtService.generateToken(any())).thenReturn("encodedPassword");
        AuthenticationResponse result = clientService.generateToken(any());
		assertNotNull(result);
    }
     
    @Test
    void it_Should_Authenticate_Token() 
    {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken("user", "password"));
        when(clientRepository.findClientByName(any(String.class)))
            .thenReturn(Optional.of(easyRandom.nextObject(ClientEntity.class)));
        assertNotNull(clientService.authenticateToken(easyRandom.nextObject(AuthenticateRequest.class)));
    }
    
    @Test
    void it_Should_Not_Authenticate_Token() 
    {
    	when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
    		.thenThrow(new RuntimeException("some error"));
    	 assertThrows(EbankingManagerException.class, () -> clientService.authenticateToken(easyRandom.nextObject(AuthenticateRequest.class))); 
    }
        
    @Test
    void it_Should_Find_Client_By_Name() 
    {
        when(clientService.findClientByName(any())).thenReturn(Optional.ofNullable(easyRandom.nextObject(ClientEntity.class)));
        assertNotNull(clientService.findClientByName(any()));
    }
    
    @Test
    void it_Should_Not_Find_Client_By_Name() 
    {
        when(clientService.findClientByName(any())).thenReturn(null);
        assertNull(clientService.findClientByName(any()));
    }
}
