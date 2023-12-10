package com.alexpages.ebankingapi.utils;

import lombok.*;
import java.util.List;

import com.alexpages.ebankingapi.entity.TransactionEntity;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedList {
    private List<TransactionEntity> transactions;
    private float debitCreditScore;
    private int pageNo;
}
