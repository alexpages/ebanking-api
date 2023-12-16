package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.repository.ClientRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public Optional<ClientEntity> findClientByName(String name){
        return clientRepository.findClientByName(name);
    }

    public ClientEntity addClient (Client client){
    	
    	
        return clientRepository.save(client);
    }

    public ClientEntity findClientByAccount(String iban){
        ClientEntity foundClient = clientRepository.findClientByAccount(iban);
        if (foundClient==null){
            throw new EbankingManagerException("Client with iban: "+ iban+ ", could not be found");
        }
        return foundClient;
    }

    public String findClientNameByAccount(String iban){
        return findClientByAccount(iban).getUsername();
    }

}
