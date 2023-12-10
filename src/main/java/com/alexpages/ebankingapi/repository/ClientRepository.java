package com.alexpages.ebankingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alexpages.ebankingapi.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
    Optional<ClientEntity> findClientByName(String name);

    @Query("SELECT a.client FROM Account a WHERE a.iban = :iban")
    ClientEntity findClientByAccount(@Param("iban") String iban);
}
