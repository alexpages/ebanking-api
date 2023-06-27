package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.models.account.Account;
import com.alexpages.ebankingapi.models.account.AccountRepository;
import com.alexpages.ebankingapi.models.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> findAccountsByClient(Client client){
        return accountRepository.findAccountsByClient(client);
    }

}
