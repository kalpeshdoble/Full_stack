package com.example.CartService.Clients;

import com.example.CartService.DTO.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("auth/validate")
    UserDto getUserDetails(@RequestHeader("Authorization") String token);
}
