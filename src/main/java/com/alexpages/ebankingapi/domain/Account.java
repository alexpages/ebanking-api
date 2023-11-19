package com.alexpages.ebankingapi.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String iban;
    private String currency;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

}
