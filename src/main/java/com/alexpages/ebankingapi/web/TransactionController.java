package com.alexpages.ebankingapi.web;


import com.alexpages.ebankingapi.entity.TransactionEntity;
import com.alexpages.ebankingapi.others.TransactionControllerRequest;
import com.alexpages.ebankingapi.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    private TransactionService transactionService;
    
    @Autowired
    public TransactionController(TransactionService transactionService)
    {
    	this.transactionService = transactionService;
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publishTransactionToTopic(@RequestBody TransactionEntity transaction){
        try{
	        	return ResponseEntity.ok(transactionService.publishTransactionToTopic(transaction));
        } catch (Exception jsonProcessingException){
            return ResponseEntity.status(500).body(jsonProcessingException.getMessage());
        }
    }


    @GetMapping("/")
    public ResponseEntity<?> getPaginatedTransactionListByUserAndDate(@RequestBody TransactionControllerRequest transactionControllerRequest){
        //Obtain query parameters
        int month = transactionControllerRequest.getMonth();
        int year = transactionControllerRequest.getYear();
        int pageSize = transactionControllerRequest.getPageSize();
        String clientName = transactionControllerRequest.getClientName();
        //Call service
        try{
            return ResponseEntity.ok(transactionService.getPaginatedTransactionListByUserAndDate(pageSize, clientName, year, month));
        } catch (Exception jsonProcessingException){
            return ResponseEntity.status(500).body(jsonProcessingException.getMessage());
        }
    }
}
