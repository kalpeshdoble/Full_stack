package com.example.WalletService.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
@FeignClient(name = "USER-SERVICE", path = "/api/user")
public interface UserServiceClient {

    @GetMapping("/validate")
    Map<String, Object> validateUser(@RequestHeader("Authorization") String token);
}
