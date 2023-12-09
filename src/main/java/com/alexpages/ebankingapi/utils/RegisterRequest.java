package com.alexpages.ebankingapi.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.alexpages.ebankingapi.domain.Account;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest 
{
    private String clientName;
    
    private List<Account> accounts;
    
    private String password;  
}
