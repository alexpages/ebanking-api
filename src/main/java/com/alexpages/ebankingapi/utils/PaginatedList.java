package com.alexpages.ebankingapi.utils;

import lombok.*;
import java.util.List;

import com.alexpages.ebankingapi.domain.Transaction;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedList {
    private List<Transaction> transactions;
    private float debitCreditScore;
    private int pageNo;
}
