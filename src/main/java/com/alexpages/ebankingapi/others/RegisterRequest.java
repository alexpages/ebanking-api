package com.alexpages.ebankingapi.others;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.alexpages.ebankingapi.entity.AccountEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest 
{
    private String clientName;
    
    private List<AccountEntity> accounts;
    
    private String password;  
}
