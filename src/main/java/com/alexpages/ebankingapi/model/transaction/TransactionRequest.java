package com.alexpages.ebankingapi.model.transaction;

import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String clientEmail;
    private String clientPassword;
    private String month;
    private int year;
}
//client shall include token in Authentication http
