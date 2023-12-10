package com.alexpages.ebankingapi.others;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionControllerRequest {
    private String clientName;
    private int year;
    private int month;
    private int pageSize;
}
