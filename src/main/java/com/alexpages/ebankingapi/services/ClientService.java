package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.exceptions.EbankingManagerException;
import com.alexpages.ebankingapi.repository.ClientRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public Optional<Client> findClientByName(String name){
        return clientRepository.findClientByName(name);
    }

    public Client addClient (Client client){
        return clientRepository.save(client);
    }

    public Client findClientByAccount(String iban){
        Client foundClient = clientRepository.findClientByAccount(iban);
        if (foundClient==null){
            throw new EbankingManagerException("Client with iban: "+ iban+ ", could not be found");
        }
        return foundClient;
    }

    public String findClientNameByAccount(String iban){
        return findClientByAccount(iban).getUsername();
    }

}
