package com.example.CartService.repository;

import com.example.CartService.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserEmail(String email);
    void deleteByUserEmail(String email);
    Optional<CartItem> findByUserEmailAndProductName(String email, String productName);

}
