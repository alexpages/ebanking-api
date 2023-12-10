package com.alexpages.ebankingapi.web;


import com.alexpages.ebankingapi.entity.TransactionEntity;
import com.alexpages.ebankingapi.others.TransactionControllerRequest;
import com.alexpages.ebankingapi.services.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Transaction Controller")
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            description = "Publish money account transactions form an user. It requires a jwt token to perform this operation",
            summary = "Publish transactions to the system",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "Transaction", description = "The transaction object to be published"
                    )
            }
    )
    @PostMapping("/publish")
    public ResponseEntity<?> publishTransactionToTopic(@RequestBody TransactionEntity transaction){
        try{
            return ResponseEntity.ok(transactionService.publishTransactionToTopic(transaction));
        } catch (Exception jsonProcessingException){
            return ResponseEntity.status(500).body(jsonProcessingException.getMessage());
        }
    }

    @Operation(
            description = "Retrieve a paginated list of money account transactions from an specific month an year of an existing user. It requires a jwt token to perform this operation",
            summary = "Gets paginated list of user transactions",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad request",
                            responseCode = "400"
                    ),
                    @ApiResponse(
                            description = "Unauthorized",
                            responseCode = "403"
                    )
            },
            parameters = {
                    @Parameter(
                            name = "transactionControllerRequest", description = "It is a custom request object, see TransactionControllerRequest.java for more info"
                    )
            }
    )
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
