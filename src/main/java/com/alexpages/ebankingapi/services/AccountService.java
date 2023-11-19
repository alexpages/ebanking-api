package com.alexpages.ebankingapi.services;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.repository.AccountRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService 
{
	
	private AccountRepository accountRepository;
	
    public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

    public List<Account> findAccountsByClient(Client client){
        return accountRepository.findAccountsByClient(client);
    }
}
