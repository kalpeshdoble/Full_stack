package com.example.ProductService.repository;

import com.example.ProductService.dto.ProductRequest;
import com.example.ProductService.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    Product findByName(String name);

}
