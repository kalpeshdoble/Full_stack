package com.example.WalletService.controller;

import com.example.WalletService.Client.UserServiceClient;
import com.example.WalletService.dto.AddFundsRequest;
//import com.example.WalletService.dto.CreateWallet;
import com.example.WalletService.dto.PaymentRequest;
import com.example.WalletService.dto.WalletResponse;
import com.example.WalletService.entity.Wallet;
import com.example.WalletService.entity.WalletTransaction;
import com.example.WalletService.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {
    @Autowired
    private  WalletService walletService;

    @PostMapping("/add")
    public ResponseEntity<Wallet> addMoney(@RequestParam String email, @RequestParam double amount) {
        return ResponseEntity.ok(walletService.addMoney(email, amount));
    }

    @PutMapping("/deduct")
    public ResponseEntity<Boolean> deductAmount(@RequestParam String email, @RequestParam double amount) {
        return ResponseEntity.ok(walletService.deductAmount(email, amount));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<WalletTransaction>> getTransactions(@RequestParam String email) {
        return ResponseEntity.ok(walletService.getTransactionHistory(email));
    }

    @GetMapping("/balance")
    public ResponseEntity<Wallet> getWallet(@RequestParam String email) {
        return ResponseEntity.ok(walletService.getWallet(email));
    }
}
