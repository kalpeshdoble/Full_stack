package com.example.WalletService.service;

import com.example.WalletService.entity.TransactionType;
import com.example.WalletService.entity.Wallet;
import com.example.WalletService.entity.WalletTransaction;
import com.example.WalletService.repository.WalletRepository;
import com.example.WalletService.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WalletService {
   @Autowired
    private  WalletRepository walletRepository;
   @Autowired
    private  WalletTransactionRepository transactionRepository;


    public Wallet addMoney(String email, double amount) {
        Wallet wallet = walletRepository.findByUserEmail(email).orElseGet(() -> {
            Wallet w = new Wallet();
            w.setUserEmail(email);
            w.setBalance(0);
            return walletRepository.save(w);
        });

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setUserEmail(email);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEBIT);
        transaction.setDescription("Order payment from wallet");
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        return wallet;
    }


    public boolean deductAmount(String email, double amount) {
        Wallet wallet = walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found for email: " + email));

        if (wallet.getBalance() < amount) return false;

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setUserEmail(email);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEBIT);
        transaction.setDescription("Order payment from wallet");
        transaction.setTimestamp(LocalDateTime.now());


        transactionRepository.save(transaction);

        return true;
    }


    public List<WalletTransaction> getTransactionHistory(String email) {
        return transactionRepository.findByUserEmail(email);
    }


    public Wallet getWallet(String email) {
        return walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }
}

