package com.alexpages.ebankingapi.controller;


import com.alexpages.ebankingapi.model.transaction.Transaction;
import com.alexpages.ebankingapi.service.TransactionService;
import com.alexpages.ebankingapi.utils.TransactionControllerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/publish")
    public ResponseEntity<?> publishTransactionToTopic(@RequestBody Transaction transaction){
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


//
//    @GetMapping("/")
//    public ResponseEntity<?> consumeTransactionsFromTopic(@RequestBody TransactionControllerRequest transactionRequest){
//        //Obtain query parameters
//        int month = transactionRequest.getMonth();
//        int year = transactionRequest.getYear();
//        String clientName = transactionRequest.getClientName();
//        //Call service
//        try{
//            return ResponseEntity.ok(transactionService.consumeTransactionsFromTopic(clientName,year,month));
//        } catch (Exception jsonProcessingException){
//            return ResponseEntity.status(500).body(jsonProcessingException.getMessage());
//        }
//    }

}
