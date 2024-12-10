package com.example.server.services;

import com.example.server.entity.TransactionEntity;
import com.example.server.repositories.TransactionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class TransactionService {

    private TransactionRepo transactionRepository;

    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public void deleteTransaction(Integer transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new IllegalArgumentException("Transaction not found.");
        }
        transactionRepository.deleteById(transactionId);
    }

    public List<TransactionEntity> getTransactionsByUserId(Integer userId) {
        return transactionRepository.findByUserId(userId);
    }
}
