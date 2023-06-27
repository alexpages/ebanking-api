package com.alexpages.ebankingapi.models.account;

import com.alexpages.ebankingapi.models.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findAccountsByClient(Client client);

}
