package com.example.Order_Service.Clients;

import com.example.Order_Service.dto.CartItem;
import com.example.Order_Service.dto.WalletDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wallet-service", url = "http://localhost:8086/wallet")
//@FeignClient(name = "wallet-service")
public interface WalletClient {

    @PutMapping("/wallet/deduct")
    boolean deductBalance(@RequestParam String email, @RequestParam double amount);

    @PostMapping("/wallet/add")
    ResponseEntity<WalletDto> addMoney(@RequestParam String email, @RequestParam double amount);

}

