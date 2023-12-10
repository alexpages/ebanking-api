package com.alexpages.ebankingapi.others;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticateRequest {
    private String clientName;
    private String password;
}
