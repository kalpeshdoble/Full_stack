package com.example.ProductService.controller;

import com.example.ProductService.client.UserServiceClient;
import com.example.ProductService.dto.ProductRequest;
import com.example.ProductService.model.Product;
import com.example.ProductService.repository.ProductRepository;
import com.example.ProductService.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/all")
    public List<Product> all() {
        return productService.getAllProducts();
    }



    @Autowired private ProductService productService;
    @Autowired
    private UserServiceClient userServiceClient;
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest request,
                                        @RequestHeader("Authorization") String token) {
        return productService.addProduct(request, token);
    }

//    @GetMapping("/all")
//    public List<Product> all() {
//        return productService.getAllProducts();
//    }

    @GetMapping("/category/{category}")
    public List<Product> byCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/{id}")
    public Product byId(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }
    @Autowired
    private ProductRepository productRepository;
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.deleteProduct(id);
    }


//    @GetMapping("/by-name")
//    public Product getProductByName(@RequestParam String name,@RequestParam int  quantity) {
//
//        Product product = productRepository.findByName(name);
//           if(product==null){
//               return null;
//           }
//        if(product.getStock()<quantity){
//            return null;
//        }
//        product.setStock(product.getStock()-quantity);
//        productRepository.save(product);
//        if (product == null) {
//            return null;
//        }
//        return new Product(product.getCategory(),product.getDescription(),product.getName(),product.getPrice(),quantity);
//    }
    @GetMapping("/by-name")
    public ResponseEntity<?> getProductByName(@RequestParam String name,
                                              @RequestParam int quantity) {
        Product result = productService.getProductByName(name, quantity);

        if (result == null) {
            return null;
        }

        return ResponseEntity.ok(result);
    }
    @PostMapping("/decreaseStock")
    public ResponseEntity<String> decreaseStock(@RequestParam String productName, @RequestParam int quantity) {
        try {
            productService.decreaseStock(productName, quantity);
            return ResponseEntity.ok("Stock updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/increaseStock")
    public ResponseEntity<String> increaseStock(@RequestParam String productName, @RequestParam int quantity) {
        try {
            productService.decreaseStock(productName, quantity);
            return ResponseEntity.ok("Stock updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/searchbyname")
    public Product searchProductByName(@RequestParam String name) {
        return productService.getProductsByName(name);
    }





}
