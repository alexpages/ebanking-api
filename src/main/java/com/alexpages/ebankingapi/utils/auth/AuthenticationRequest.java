package com.alexpages.ebankingapi.utils.auth;

import com.alexpages.ebankingapi.model.account.Account;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;
    private List<Account> accounts;

}
