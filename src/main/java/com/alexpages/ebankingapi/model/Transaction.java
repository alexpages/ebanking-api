package com.alexpages.ebankingapi.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Transaction {

    private String identifier;          // Unique identifier (e.g. 89d3o179-abcd-465b-o9ee-e2d5f6ofEld46)
    private String amountCurrency;      // Amount with currency (eg GBP 100-, CHF 75)
    private String IBAN;                // Account IBAN (eg. CH93-0000-0000-0000-0000-0)
    private LocalDateTime valueDate;    // Value date (e.g. 01-10-2020)
    private String description;         // Description (e.g. Online payment CHF)

}
