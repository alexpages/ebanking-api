package com.alexpages.ebankingapi.model.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;          // Unique identifier (e.g. 89d3o179-abcd-465b-o9ee-e2d5f6ofEld46)
    @Enumerated(EnumType.STRING)
    @JsonProperty("currency")
    private Currency currency;           // Amount with currency (eg GBP 100-, CHF 75)
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("iban")
    private String iban;                // Account IBAN (eg. CH93-0000-0000-0000-0000-0)
    @JsonProperty("valueDate")
    private String valueDate;    // Value date (e.g. 01-10-2020)
    @JsonProperty("description")
    private String description;         // Description (e.g. Online payment CHF)
}
