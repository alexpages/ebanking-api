package com.alexpages.ebankingapi.web;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.alexpages.ebankingapi.api.EbankingApi;
import com.alexpages.ebankingapi.domain.AddClient201Response;
import com.alexpages.ebankingapi.domain.AddTransaction201Response;
import com.alexpages.ebankingapi.domain.AuthenticateRequest;
import com.alexpages.ebankingapi.domain.AuthenticateResponse;
import com.alexpages.ebankingapi.domain.Client;
import com.alexpages.ebankingapi.domain.InputDataSearch;
import com.alexpages.ebankingapi.domain.OutputDataSearch;
import com.alexpages.ebankingapi.domain.PageResponse;
import com.alexpages.ebankingapi.domain.Score;
import com.alexpages.ebankingapi.domain.Transaction;
import com.alexpages.ebankingapi.entity.ClientEntity;
import com.alexpages.ebankingapi.error.EbankingManagerException;
import com.alexpages.ebankingapi.service.ClientService;
import com.alexpages.ebankingapi.service.TransactionService;
import com.alexpages.ebankingapi.utils.PageableUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EBankingController implements EbankingApi{
	
	private final ClientService clientService;
	private final TransactionService transactionService;
	
	@Override
	public ResponseEntity<AddClient201Response> addClient(@Valid Client client) 
	{
		try 
		{
			ClientEntity clientEntity = clientService.addClient(client);		
			AddClient201Response response = new AddClient201Response();
			response.setClientId(clientEntity.getUsername());
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
		catch(Exception e)
		{	
			log.error("EBankingController > addClient > Error when adding the client: " + client);
			throw new EbankingManagerException("Error when adding client");
		}
	}
	
	@Override
	public ResponseEntity<AuthenticateResponse> authenticateClient(@Valid AuthenticateRequest authenticateRequest) 
	{
		try 
		{
			AuthenticateResponse response = clientService.authenticateToken(authenticateRequest);
			log.info("EBankingController > authenticateClient > AuthenticateResponse: {}", response.toString());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		catch(Exception e)
		{
			log.error("EBankingController > authenticateClient > Error when authenticatio the client: " + authenticateRequest.getUsername());
			throw new EbankingManagerException("Error when authenticating client");
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
	public ResponseEntity<OutputDataSearch> getTransactions(@Valid InputDataSearch inputDataSearch) 
	{
		try 
		{
			Page<Transaction> transactionPage = transactionService.getTransactions(inputDataSearch);
			if(transactionPage.getContent().isEmpty()) 
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} 
			PageResponse pageResponse = PageableUtils.getPaginationResponse(transactionPage, transactionPage.getPageable());
			OutputDataSearch response = new OutputDataSearch();
			response.setPagination(pageResponse);
			response.setTransaction(transactionPage.getContent());
			Score score = new Score();
			score.setAmount(transactionService.calculateDebitCreditScore(transactionPage.getContent()));
			response.setScore(score);
			return new ResponseEntity<>(response, HttpStatus.OK);		
		}
		catch(Exception e)
		{
			throw new EbankingManagerException("EBankingController > getTransactions > Error when getting paginated transaction list");
		}
	}
}