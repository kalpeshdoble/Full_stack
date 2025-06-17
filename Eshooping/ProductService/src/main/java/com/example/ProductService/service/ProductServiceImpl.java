package com.example.ProductService.service;

import com.example.ProductService.client.UserServiceClient;
import com.example.ProductService.dto.ProductRequest;
import com.example.ProductService.model.Product;
import com.example.ProductService.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired private ProductRepository repo;

//    @Override
//    public Product addProduct(Product product) {
//        return repo.save(product);
//    }
    @Autowired
    private UserServiceClient userServiceClient;

    public ResponseEntity<?> addProduct(ProductRequest request, String token) {
        Map<String, Object> userData = userServiceClient.validateUser(token);
        String role = (String) userData.get("role");

        System.out.println("Token received in Product Service: " + token);

        if (!"MERCHANT".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access denied: Only MERCHANT can add products.");
        }
        Product existingProduct = repo.findByName(request.getName());
        if (existingProduct != null) {
            existingProduct.setStock(existingProduct.getStock() + request.getStock());
            repo.save(existingProduct);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Product already exists. Stock updated.");
        }

        Product newProduct = new Product(
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getStock(),
                (String) userData.get("email"),
                request.getImgUrl()
        );

        return ResponseEntity.ok(repo.save(newProduct));
    }
    public Product getProductByName(String name, int quantity) {
        Product product = repo.findByName(name);

        if(product == null){
            return null;
        }

        if (product.getStock() < quantity) {
            return null;
        }
        // Return only the requested quantity (a new product object for response)
        return new Product(
                product.getCategory(),
                product.getDescription(),
                product.getName(),
                product.getPrice(),
                quantity
        );
    }
    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return repo.findByCategory(category);
    }

    @Override
    public Product getProductsByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public Product getProductById(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setCategory(updatedProduct.getCategory());
        return repo.save(product);
    }
    @Transactional
    public void decreaseStock(String productName, int quantity) {
        Product product = repo.findByName(productName);

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productName);
        }

        product.setStock(product.getStock() - quantity);
        repo.save(product);
    }
    @Transactional
    public void increaseStock(String productName, int quantity) {
        Product product = repo.findByName(productName);



        product.setStock(product.getStock() +quantity);
        repo.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}
