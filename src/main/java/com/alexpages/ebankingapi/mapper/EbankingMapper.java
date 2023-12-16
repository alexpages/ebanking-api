package com.alexpages.ebankingapi.mapper;

import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.entity.ClientEntity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EbankingMapper {
	
	EbankingMapper INSTANCE = Mappers.getMapper(EbankingMapper.class);

	Client toClient(ClientEntity clientEntity);
	
	ClientEntity toClientEntity(Client client);
	
}
