package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

	@Autowired
    private AccountRepository accountRepository;

    public List<Account> findAccountsByClient(Client client){
        return accountRepository.findAccountsByClient(client);
    }
}
