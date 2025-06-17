package com.example.WalletService.repository;

import com.example.WalletService.entity.Wallet;
import com.example.WalletService.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findByUserEmail(String userEmail);
}