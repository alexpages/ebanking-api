package com.alexpages.ebankingapi.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Data
@Component
public class Account {
    private String IBAN;
    private String currency;
}
