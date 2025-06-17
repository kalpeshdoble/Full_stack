package com.example.Order_Service.Clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @PostMapping("/product/decreaseStock")
    String decreaseStock(@RequestParam String productName, @RequestParam int quantity);

    @PostMapping("/product/increaseStock")
    String increaseStock(@RequestParam String productName, @RequestParam int quantity);
}