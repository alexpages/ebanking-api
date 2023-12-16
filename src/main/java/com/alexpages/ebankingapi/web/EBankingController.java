package com.alexpages.ebankingapi.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.alexpages.ebankingapi.api.EbankingApi;
import com.alexpages.ebankingapi.domain.AddClient201Response;
import com.alexpages.ebankingapi.domain.AddTransaction201Response;
import com.alexpages.ebankingapi.domain.AuthenticateClient200Response;
import com.alexpages.ebankingapi.domain.AuthenticateRequest;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.domain.InputDataSearch;
import com.alexpages.ebankingapi.domain.OutputDataSearch;
import com.alexpages.ebankingapi.domain.Transaction;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.service.ClientService;
import com.alexpages.ebankingapi.service.TransactionService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class EBankingController implements EbankingApi{
	
	private ClientService clientService;
	private TransactionService transactionService;
	
	@Autowired
	public EBankingController(ClientService clientService, TransactionService transactionService)
	{
		this.clientService 		= clientService;
		this.transactionService = transactionService;
	}
	
	@Override
	public ResponseEntity<AddClient201Response> addClient(@Valid Client client) 
	{
		try 
		{
			//TODO Perform client validations
			ClientEntity clientEntity = clientService.addClient(client);		
			AddClient201Response response = new AddClient201Response();
			response.setClientName(clientEntity.getUsername());
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		catch(Exception e)
		{	
			log.error("EBankingController > addClient > Error when adding the client: " + client);
			throw new EbankingManagerException("Error when adding client");
		}
	}

	@Override
	public ResponseEntity<AddTransaction201Response> addTransaction(@Valid Transaction transaction) 
	{
		try 
		{	
			//TODO Perform transaction validations
			Transaction addedTransaction = transactionService.publishTransactionToTopic(transaction);
			AddTransaction201Response response = new AddTransaction201Response();
			response.setTransactionId(addedTransaction.getTransactionId());
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		catch(Exception e)
		{
			log.error("EBankingController > addTransaction > Error when adding the transaction: " + transaction);
			throw new EbankingManagerException("Error when adding transaction");
		}
	}

	@Override
	public ResponseEntity<AuthenticateClient200Response> authenticateClient(Integer clientId,
			@Valid AuthenticateRequest authenticateRequest) {
		
		try 
		{
			//TODO Perform client validations
			
			
		}
		catch(Exception e)
		{
			// TODO APA - Handle Exception
		}
		return null;
	}

	@Override
	public ResponseEntity<OutputDataSearch> getTransactions(@Valid InputDataSearch inputDataSearch) {
		
		try 
		{
			
			
		}
		catch(Exception e)
		{
			// TODO APA - Handle Exception
			
		}
		return null;
	}
}
