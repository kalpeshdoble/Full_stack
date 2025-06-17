package com.example.CartService.Clients;

import com.example.CartService.DTO.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/product/by-name")
    ProductDto getProductByName(@RequestParam("name") String name,@RequestParam("quantity") int quantity);
}
