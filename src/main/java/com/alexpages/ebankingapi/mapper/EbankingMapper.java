package com.alexpages.ebankingapi.mapper;

import com.alexpages.ebankingapi.domain.Account;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.entity.AccountEntity;
import com.alexpages.ebankingapi.entity.ClientEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EbankingMapper {
	
	EbankingMapper INSTANCE = Mappers.getMapper(EbankingMapper.class);

	@Mapping(target = "accounts", source = "accounts", qualifiedByName = "toAccount")
	Client toClient(ClientEntity clientEntity);
	
	@Mapping(target = "accounts", source = "accounts", qualifiedByName = "toAccountEntity")
	ClientEntity toClientEntity(Client client);
	
	@Named("toAccountEntity")
	AccountEntity toAccountEntity(Account account);
	
	@Named("toAccount")
	Account toAccount(AccountEntity accountEntity);

}
