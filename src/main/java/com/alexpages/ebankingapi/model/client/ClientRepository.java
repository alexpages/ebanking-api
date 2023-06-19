package com.alexpages.ebankingapi.model.client;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByEmail(String email);

}
