package com.alexpages.ebankingapi.web;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import com.alexpages.ebankingapi.api.EbankingApi;
import com.alexpages.ebankingapi.domain.AddClient201Response;
import com.alexpages.ebankingapi.domain.AddTransaction201Response;
import com.alexpages.ebankingapi.domain.AuthenticateClient200Response;
import com.alexpages.ebankingapi.domain.AuthenticateRequest;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.domain.InputDataSearch;
import com.alexpages.ebankingapi.domain.OutputDataSearch;
import com.alexpages.ebankingapi.domain.Transaction;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EBankingController implements EbankingApi{

	@Override
	public ResponseEntity<AddClient201Response> addClient(@Valid Client client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<AddTransaction201Response> addTransaction(@Valid Transaction transaction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<AuthenticateClient200Response> authenticateClient(BigDecimal clientId,
			@Valid AuthenticateRequest authenticateRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<OutputDataSearch> getTransactions(@Valid InputDataSearch inputDataSearch) {
		// TODO Auto-generated method stub
		return null;
	}

}
