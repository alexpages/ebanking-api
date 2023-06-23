package com.alexpages.ebankingapi.utils;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private String clientName;
    private int year;
    private int month;

}
