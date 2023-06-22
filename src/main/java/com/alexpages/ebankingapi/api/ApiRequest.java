package com.alexpages.ebankingapi.api;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequest {
    private String email;
}
