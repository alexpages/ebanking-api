package com.alexpages.ebankingapi.utils.auth;

import com.alexpages.ebankingapi.models.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String clientName;
    private List<Account> accounts;
    private String password;
}
