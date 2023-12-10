package com.alexpages.ebankingapi.others;

import lombok.*;
import java.util.List;

import com.alexpages.ebankingapi.utils.PaginatedList;

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
