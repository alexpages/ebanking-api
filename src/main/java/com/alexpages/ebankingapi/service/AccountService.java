package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.model.account.Account;
import com.alexpages.ebankingapi.model.account.AccountRepository;
import com.alexpages.ebankingapi.model.client.Client;
import com.alexpages.ebankingapi.model.client.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public List<Account> findAccountsByClient(Client client){
        return accountRepository.findAccountsByClient(client);
    }
//
//    public List<Account> getAccountsByClientUsername(String username) {
//        Optional<Client> client = clientService.findClientByName(username);
//        if (client.isPresent()) {
//            return accountRepository.findAccountsByClient(client.get());
//        }
//        // Log
//        logger.warn("Client with username {} has no accounts registered", username);
//        return Collections.emptyList();
//    }

}
