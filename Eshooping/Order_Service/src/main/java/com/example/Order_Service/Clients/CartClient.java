package com.example.Order_Service.Clients;

import com.example.Order_Service.dto.CartItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "cart-service", url = "http://localhost:8084/cart")
public interface CartClient {

    @GetMapping
    List<CartItem> getCartItems(@RequestHeader("Authorization") String token);
}
