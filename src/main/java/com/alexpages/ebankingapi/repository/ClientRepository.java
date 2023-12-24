package com.alexpages.ebankingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import com.alexpages.ebankingapi.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository 
extends JpaRepository<ClientEntity, Integer>,
		PagingAndSortingRepository<ClientEntity, Integer>, 
		QueryByExampleExecutor<ClientEntity>
{
	@Query("SELECT c FROM ClientEntity c WHERE c.name = :name")
	Optional<ClientEntity> findClientByName(@Param("name") String name);

    @Query("SELECT c.client FROM Account c WHERE c.iban = :iban")
    Optional<ClientEntity> findClientByAccount(@Param("iban") String iban);
}
