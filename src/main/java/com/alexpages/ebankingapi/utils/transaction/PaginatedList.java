package com.alexpages.ebankingapi.utils.transaction;

import com.alexpages.ebankingapi.model.transaction.Transaction;
import lombok.*;
import java.util.List;

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
