package com.alexpages.ebankingapi.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.alexpages.ebankingapi.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository 
extends PagingAndSortingRepository<ClientEntity, Integer>, 
		QueryByExampleExecutor<ClientEntity>{

	Optional<ClientEntity> findClientByName(String name);

    @Query("SELECT a.client FROM Account a WHERE a.iban = :iban")
    ClientEntity findClientByAccount(@Param("iban") String iban);
}
