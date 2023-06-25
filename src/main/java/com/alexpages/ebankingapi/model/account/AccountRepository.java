package com.alexpages.ebankingapi.model.account;

import com.alexpages.ebankingapi.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findAccountsByClient(Client client);

}
