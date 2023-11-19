package com.alexpages.ebankingapi.domain;

import com.alexpages.ebankingapi.utils.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    private String id;                      // Unique identifier (e.g. 89d3o179-abcd-465b-o9ee-e2d5f6ofEld46)
    @Enumerated(EnumType.STRING)
    private Currency currency;              // currency (eg GBP 100-, CHF 75)
    private double amount;                  // Amount with currency (eg GBP 100-, CHF 75)
    private String iban;                    // Account IBAN (eg. CH93-0000-0000-0000-0000-0)
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "dd-MM-yyyy")
    public Date date;                       // Value date (e.g. 01-10-2020)
    private String description;             // Description (e.g. Online payment CHF)
}
