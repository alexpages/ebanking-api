package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.domain.Client.RoleEnum;
import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.mapper.EbankingMapper;
import com.alexpages.ebankingapi.others.AuthenticateRequest;
import com.alexpages.ebankingapi.others.AuthenticationResponse;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.repository.ClientRepository;
import com.alexpages.ebankingapi.service.ClientService;
import com.alexpages.ebankingapi.service.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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
    
	@BeforeEach
	public void setupBeforeEach() {
		MockitoAnnotations.openMocks(this);
	}
    
    @Test
    void it_Should_Add_Client() 
    {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(ebankingMapper.toClientEntity(any())).thenReturn(generateValidClientEntity());
    	when(clientRepository.save(any())).thenReturn(generateValidClientEntity());
		ClientEntity result = clientService.addClient(generateValidClient());
		assertNotNull(result);
    }
    
    @Test
    void it_Should_Not_Add_Client() 
    {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(ebankingMapper.toClientEntity(any())).thenReturn(generateValidClientEntity());
    	when(clientRepository.save(any())).thenThrow(new RuntimeException("some error"));
		assertThrows(EbankingManagerException.class, () -> clientService.addClient(any()));
    }
    
    @Test
    void it_Should_Generate_Token() 
    {
        when(jwtService.generateToken(any())).thenReturn("encodedPassword");
        AuthenticationResponse result = clientService.generateToken(any());
		assertNotNull(result);
    }
    
    
//    @Test
//    void it_Should_Authenticate_Token() 
//    {
//        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
//        when(ebankingMapper.toClientEntity(any())).thenReturn(generateValidClientEntity());
//    	when(clientRepository.save(any())).thenReturn(generateValidClientEntity());
//		
//    	AuthenticationResponse result = clientService.authenticateToken(any());
//		assertNotNull(result);
//    }
//   
    
//    public AuthenticationResponse authenticateToken(AuthenticateRequest request) 
//    {
//    	String clientName= request.getClientName();
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                        request.getClientName(),
//                        request.getPassword()));
//        
//        Optional<ClientEntity> clientOptional = clientRepository.findClientByName(clientName);
//	    if (clientOptional.isPresent())
//	    {
//	    	log.error("ClientService > authenticateToken > Client: " + clientName + ", could not be authenticated");
//	        throw new EbankingManagerException("Client could not be registered");
//	    }  
//	    log.info("ClientService > authenticateToken > Client authenticated successfully: {}", clientOptional.get().toString());
//        return generateToken(clientOptional.get());
//    }
//    
//    
//    
    
    
    

    @Test
    void it_Should_Find_Client_By_Name() 
    {
        when(clientService.findClientByName(any())).thenReturn(Optional.ofNullable(generateValidClientEntity()));
        assertNotNull(clientService.findClientByName(any()));
    }
    
    @Test
    void it_Should_Not_Find_Client_By_Name() 
    {
        when(clientService.findClientByName(any())).thenReturn(null);
        assertNull(clientService.findClientByName(any()));
    }
    
    private ClientEntity generateValidClientEntity() 
    {
        ClientEntity testClient = ClientEntity.builder()
                .clientRole(ClientRole.USER)
                .clientId(1)
                .name("test")
                .password("test")
                .build();
        AccountEntity testAccount = AccountEntity.builder()
                .bankId(1)
                .iban("iban")
                .currency("EUR")
                .client(testClient)
                .build();
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(testAccount);
        testClient.setAccounts(accounts);
        return testClient;
    }
    
    private Client generateValidClient()
    {
    	Client client = new Client();
    	client.setClientId(1);
    	client.setName("name");
    	client.setPassword("password");
    	client.setRole(RoleEnum.USER);
    	client.setAccounts(generateValidListOfAccounts()); 	
    	return client;
    }
    
    private List<Account> generateValidListOfAccounts()
    {
    	List<Account> accountList = new ArrayList<>();
    	Account account = new Account();
    	account.setBankId(1);
    	account.setClient(null);
    	account.setCurrency("EUR");
    	account.setIban("IBAN");
    	accountList.add(account);
    	return accountList;
    }
}
