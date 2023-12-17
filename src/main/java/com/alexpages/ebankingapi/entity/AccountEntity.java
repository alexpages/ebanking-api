package com.alexpages.ebankingapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bankId;
    private String iban;
    private String currency;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;
}
