package com.alexpages.ebankingapi.utils.auth;

import com.alexpages.ebankingapi.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private List<Account> accounts;
    private String password;
}
