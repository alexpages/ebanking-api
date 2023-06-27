package com.alexpages.ebankingapi.utils.auth;

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
