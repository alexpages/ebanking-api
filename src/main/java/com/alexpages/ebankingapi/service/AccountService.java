package com.alexpages.ebankingapi.service;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountService 
{
	private AccountRepository accountRepository;
	
	@Autowired
    public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

    public List<AccountEntity> findAccountsByClient(ClientEntity client){
        return accountRepository.findAccountsByClient(client);
    }
}
