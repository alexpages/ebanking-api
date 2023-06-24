package com.alexpages.ebankingapi.utils;

import com.alexpages.ebankingapi.model.transaction.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedList {
    private List<Transaction> transactions;
    private float debitCreditScore;
    private int pageNo;
}
