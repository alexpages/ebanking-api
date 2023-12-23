package com.alexpages.ebankingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;

import java.util.List;

@Repository
public interface AccountRepository 
extends JpaRepository<AccountEntity, Integer> 
{
    List<AccountEntity> findAccountsByClient(ClientEntity client);
}
