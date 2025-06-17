package com.example.WalletService.dto;

import lombok.Data;

@Data
public class AddFundsRequest {
    private String email;
    private double amount;

    // Getters and Setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
