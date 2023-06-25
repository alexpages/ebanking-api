package com.alexpages.ebankingapi.utils;

import lombok.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionControllerResponse {
    private int pageSize;
    private List<PaginatedList> content;
    private int totalPages;
}
