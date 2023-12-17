package com.alexpages.ebankingapi.others;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class AuthenticationResponse {
    private String token;
}
