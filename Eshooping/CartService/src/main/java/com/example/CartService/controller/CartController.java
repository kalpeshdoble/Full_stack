package com.example.CartService.controller;

import com.example.CartService.Clients.ProductClient;
import com.example.CartService.Clients.UserClient;
import com.example.CartService.DTO.*;
import com.example.CartService.model.CartItem;
import com.example.CartService.repository.CartRepository;
import com.example.CartService.service.CartService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/cart")
//@RequiredArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserClient userClient;
    private final ProductClient productClient;
    public CartController(CartRepository cartRepository, UserClient userClient, ProductClient productClient,CartService cartService) {
        this.cartRepository = cartRepository;
        this.userClient = userClient;
        this.productClient = productClient;
        this.cartService=cartService;
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(
            @RequestHeader("Authorization") String token,
            @RequestBody CartItemRequest request) {

        ApiResponse response = cartService.addToCart(token, request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response); // 200 OK
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response); // 400 Bad Request
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems( @RequestHeader("Authorization") String token) {
        UserDto user = userClient.getUserDetails(token);
        return ResponseEntity.ok(cartRepository.findByUserEmail(user.getEmail()));
    }
@Transactional
        @DeleteMapping("/clear")
    public String clearCart(@RequestParam String email) {
            return  cartService.clearCart(email);
    }
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<Map<String, String>> removeItem(@PathVariable Long itemId,
                                                          @RequestHeader("Authorization") String token) {
        try {
            String msg = cartService.removeItem(token, itemId);
            Map<String, String> res = new HashMap<>();
            res.put("message", msg);
            return ResponseEntity.ok(res); // ✅ clean JSON response
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }


    @PutMapping("/increase")
    public ResponseEntity<?> increaseQuantity(@RequestParam String email,
                                                     @RequestParam String productName) {
        CartItem updated = cartService.increaseQuantity(email, productName);
        if (updated == null) {
            return ResponseEntity.ok(new apiresponse2("Item removed from cart."));
        }
        return ResponseEntity.ok(updated);
    }


    @PutMapping("/decrease")
    public ResponseEntity<?> decreaseQuantity(@RequestParam String email,
                                              @RequestParam String productName) {
        CartItem updated = cartService.decreaseQuantity(email, productName);
        if (updated == null) {
            return ResponseEntity.ok(new apiresponse2("Item removed from cart."));
        }
        return ResponseEntity.ok(updated);
    }

    @Transactional
    @DeleteMapping("/checkout")
    public String checkout(@RequestParam String email) {
        return cartService.checkout(email);
    }

}
