package com.alexpages.ebankingapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String identifier;          // Unique identifier (e.g. 89d3o179-abcd-465b-o9ee-e2d5f6ofEld46)
    private String amountCurrency;      // Amount with currency (eg GBP 100-, CHF 75)
    private String IBAN;                // Account IBAN (eg. CH93-0000-0000-0000-0000-0)
    private LocalDateTime valueDate;    // Value date (e.g. 01-10-2020)
    private String description;         // Description (e.g. Online payment CHF)
}
