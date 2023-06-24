package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.account.AccountRepository;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;

    public List<Account> findAccountsByClient(Client client){
        return accountRepository.findAccountsByClient(client);
    }

    public Client findClientByAccount(String iban){
        return accountRepository.findClientByAccount(iban);
    }

    public List<Account> getAccountsByClientUsername(String email) {
        Optional<Client> client = clientService.findClientByName(email);
        if (client.isPresent()) {
            return accountRepository.findAccountsByClient(client.get());
        }
        return Collections.emptyList();
    }

    public Client getClientByAccount(String iban){
        return accountRepository.findClientByAccount(iban);
    }

}
