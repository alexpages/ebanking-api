package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.repository.ClientRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Optional<Client> findClientByName(String name){
        return clientRepository.findClientByName(name);
    }

    public Client addClient (Client client){
        return clientRepository.save(client);
    }

    public Client findClientByAccount(String iban){
        Client foundClient = clientRepository.findClientByAccount(iban);
        if (foundClient==null){
            throw new RuntimeException("Client with iban: "+ iban+ ", could not be found");
        }
        return foundClient;
    }

    public String findClientNameByAccount(String iban){
        return findClientByAccount(iban).getUsername();
    }

}
