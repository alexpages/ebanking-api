package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
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
        return clientRepository.findClientByAccount(iban);
    }


}
