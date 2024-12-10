package com.example.server.controllers;

import com.example.server.entity.TransactionEntity;
import com.example.server.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private TransactionService transactionService;

    @GetMapping("/getAll")
    public ResponseEntity<List<TransactionEntity>> getAllTransactions() {
        try {
            List<TransactionEntity> transactions = transactionService.getAllTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/delete/{transactionId}")
    public void deleteTransaction(@PathVariable Integer transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    @GetMapping("/myTransactionsGet/{userId}")
    public ResponseEntity<List<TransactionEntity>> getTransactionsByUserId(@PathVariable Integer userId) {
        List<TransactionEntity> transactions = transactionService.getTransactionsByUserId(userId);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }
}
