package com.example.Order_Service.controller;

import com.example.Order_Service.dto.*;
import com.example.Order_Service.model.Address;
import com.example.Order_Service.model.Order;
//import com.example.Order_Service.model.Orders;
import com.example.Order_Service.repository.OrderRepository;
import com.example.Order_Service.service.OrderService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
      @Autowired
    private  OrderService orderService;

    @PostMapping("/place-from-cart/cod")
    public ResponseEntity<?> placeOrderCOD(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.placeOrderFromCartCOD(token));
    }

    @PostMapping("/place-from-cart/wallet")
    public ResponseEntity<?> placeOrderWallet(@RequestHeader("Authorization") String token) {
        System.out.println("Wallet order endpoint called.");
        return ResponseEntity.ok(orderService.placeOrderFromCartWallet(token));
    }

    @PostMapping("/address")
    public ResponseEntity<?> saveAddress(@RequestBody Address address) {
        return ResponseEntity.ok(orderService.saveAddress(address));
    }

    @PutMapping("/address")
    public ResponseEntity<?> updateAddress(@RequestBody Address address) {
        return ResponseEntity.ok(orderService.updateAddress(address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted");
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Order cancelled");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable String email) {
        return ResponseEntity.ok(orderService.getOrdersByUser(email));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
