package com.alexpages.ebankingapi.utils;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private int pageSize;
    private List<PaginatedList> content;
    private int totalPages;
}
