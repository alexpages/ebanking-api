package com.alexpages.ebankingapi.model.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findClientByName(String name);

    @Query("SELECT a.client FROM Account a WHERE a.iban = :iban")
    Client findClientByAccount(@Param("iban") String iban);
}
