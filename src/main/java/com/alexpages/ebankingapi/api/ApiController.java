package com.alexpages.ebankingapi.api;


import com.alexpages.ebankingapi.model.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/controller")
public class ApiController {

    private final ApiService apiService;

    @Autowired
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/getTransactions")
    public ResponseEntity<Transaction> getTransactionsByEmail(@RequestBody ApiRequest request) {
        return ResponseEntity.ok(apiService.getTransactionsByEmail());
    }

}
