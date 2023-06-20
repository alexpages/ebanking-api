package com.alexpages.ebankingapi.model.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private List<Transaction> transactionList;
    private double creditValue;
    private double debitValue;
}

//    Page number or identifier
//    List of transactions on the page
//    Total credit value for the transactions on the page
//     Total debit value for the transactions on the page