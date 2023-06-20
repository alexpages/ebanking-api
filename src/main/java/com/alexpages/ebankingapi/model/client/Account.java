package com.alexpages.ebankingapi.model.client;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    private Integer id;
    private String iban; //TODO add checks for not having duplicated IBAN
    private String currency;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private Client client;
}
