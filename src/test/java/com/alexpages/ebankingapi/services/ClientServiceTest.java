package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.domain.Client.RoleEnum;
import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.others.ClientRole;
import com.alexpages.ebankingapi.repository.ClientRepository;
import com.alexpages.ebankingapi.service.ClientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
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
    
	@BeforeEach
	public void setupBeforeEach() {
		MockitoAnnotations.openMocks(this);
	}
    
    @Test
    void it_Should_Add_Client() 
    {
        ClientEntity clientEntity = generateValidClientEntity();
        when(clientRepository.findClientByName(any())).thenReturn(Optional.ofNullable(clientEntity));
        when(clientRepository.save(any())).thenReturn(clientEntity);
        assertNotNull(clientService.addClient(any()));
    }
    
    void it_Should_Not_Add_Client() 
    {
        when(clientRepository.findClientByName(any())).thenReturn(Optional.ofNullable(null));
        assertNotNull(clientService.addClient(any()));
    }
    
    void it_Should_Not_Add_Client_2() 
    {
        ClientEntity clientEntity = generateValidClientEntity();
        when(clientRepository.findClientByName(any())).thenReturn(Optional.ofNullable(clientEntity));
        when(clientRepository.save(any())).thenThrow(new RuntimeException("some error"));
        assertThrows(EbankingManagerException.class, () -> clientService.addClient(any()));
    }
    
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
    
    @Test
    void it_Should_Find_Client_By_Account() 
    {
        when(clientService.findClientByAccount(any())).thenReturn(generateValidClientEntity());
        assertNotNull(clientService.findClientByAccount(any()));
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
