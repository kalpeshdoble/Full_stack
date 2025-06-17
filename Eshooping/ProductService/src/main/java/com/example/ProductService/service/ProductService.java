package com.example.ProductService.service;

import com.example.ProductService.dto.ProductRequest;
import com.example.ProductService.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    public ResponseEntity<?> addProduct(ProductRequest request, String token);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    Product getProductsByName(String name);

    Product getProductById(Long id);

    Product updateProduct(Long id, Product updatedProduct);

    void deleteProduct(Long id);

    Product getProductByName(String name, int quantity);

    void decreaseStock(String productName, int quantity);
}
