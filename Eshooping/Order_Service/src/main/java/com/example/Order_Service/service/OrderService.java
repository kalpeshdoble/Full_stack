package com.example.Order_Service.service;

import com.example.Order_Service.Clients.CartClient;
import com.example.Order_Service.Clients.UserServiceClient;
//import com.example.Order_Service.Clients.ProductClient;
import com.example.Order_Service.Clients.ProductClient;
import com.example.Order_Service.Clients.WalletClient;
import com.example.Order_Service.dto.*;
import com.example.Order_Service.exception.OrderException;
import com.example.Order_Service.model.Address;
import com.example.Order_Service.model.Order;
//import com.example.Order_Service.model.Orders;
//import com.example.Order_Service.model.Orders;
import com.example.Order_Service.repository.AddressRepository;
import com.example.Order_Service.repository.OrderRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderService {
@Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private  AddressRepository addressRepository;

 @Autowired
    private ProductClient productClient;
    @Autowired
    private  CartClient cartClient;
    @Autowired
    private  WalletClient walletClient;

    @Autowired
    private UserServiceClient userServiceClient;

    public List<Order> placeOrderFromCartCOD(String token) {
        return placeOrder(token, "COD");
    }


    public List<Order> placeOrderFromCartWallet(String token) {
        return placeOrder(token, "WALLET");
    }

    private List<Order> placeOrder(String token, String method) {
        List<CartItem> cartItems = cartClient.getCartItems(token);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty.");
        }
        Map<String, Object> userData = userServiceClient.validateUser(token);
        String email = (String) userData.get("email");


        Address address = addressRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Please store your address before placing an order."));

        List<Order> orders = new ArrayList<>();
        double totalWalletAmount = 0;

        for (CartItem item : cartItems) {
            //productClient.decreaseStock(item.getProductName(), item.getQuantity());

            Order order = new Order();
            order.setUserEmail(email);
            order.setProductName(item.getProductName());
            order.setQuantity(item.getQuantity());
            order.setTotalAmount(item.getPrice() * item.getQuantity());
            order.setPaymentMethod(method);
            order.setAddress(fullAddressString(address));
            order.setStatus("SUCCESS");

            if ("WALLET".equalsIgnoreCase(method)) {
                totalWalletAmount += order.getTotalAmount();
            }

            orders.add(orderRepository.save(order));
        }

        if ("WALLET".equalsIgnoreCase(method)) {
            boolean deducted = walletClient.deductBalance(email, totalWalletAmount);
            if (!deducted) {
                throw new RuntimeException("Insufficient wallet balance.");
            }
        }

        return orders;
    }

    private String fullAddressString(Address address) {
        return address.getFullName() + ", Flat No. " + address.getFlatNumber() + ", " +
                address.getCity() + ", " + address.getState() + " - " + address.getPincode();
    }


    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getPaymentMethod().equals("WALLET")) {
            walletClient.addMoney(order.getUserEmail(), order.getTotalAmount());
        }
       // productClient.increaseStock(order.getProductName(), order.getQuantity());
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }


    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }


    public List<Order> getOrdersByUser(String email) {
        return orderRepository.findByUserEmail(email);
    }


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }


    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(Address address) {
        return addressRepository.save(address);
    }
}
