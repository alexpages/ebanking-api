package com.alexpages.ebankingapi.model.account;

import com.alexpages.ebankingapi.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByClient(Client client);

    @Query("SELECT a.client FROM Account a WHERE a.iban = :iban")
    Client findClientByAccount(@Param("iban") String iban);
}
