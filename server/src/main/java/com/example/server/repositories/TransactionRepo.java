package com.example.server.repositories;

import com.example.server.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionEntity, Integer> {
    List<TransactionEntity> findByUserId(Integer userId);
}
