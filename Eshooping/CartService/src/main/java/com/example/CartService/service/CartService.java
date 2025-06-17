package com.example.CartService.service;

import com.example.CartService.Clients.ProductClient;
import com.example.CartService.Clients.UserClient;
import com.example.CartService.DTO.ApiResponse;
import com.example.CartService.model.CartItem;
import com.example.CartService.DTO.CartItemRequest;
import com.example.CartService.DTO.ProductDto;
import com.example.CartService.DTO.UserDto;
import com.example.CartService.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    public ApiResponse addToCart(String token, CartItemRequest request) {
        // Fetch user details from UserService
        UserDto user = userClient.getUserDetails(token);
        if (user == null || !"CUSTOMER".equalsIgnoreCase(user.getRole())) {
            return new ApiResponse("Only customers can access the cart.", false);
        }

        // Validate product from Product Service
        ProductDto product = productClient.getProductByName(request.getName(), request.getQuantity());

        if (product == null) {
            return new ApiResponse("Product not found or out of stock", false);
        }

        // Save to cart
        CartItem item = new CartItem();
        item.setUserEmail(user.getEmail());
        item.setProductName(request.getName());
        item.setQuantity(request.getQuantity());
        item.setPriceSnapshot(product.getPrice());

        cartRepository.save(item);
        return new ApiResponse("Product added to cart successfully.", true);
    }


    public List<CartItem> getCart(String token) {
        UserDto user = userClient.getUserDetails(token);
        return cartRepository.findByUserEmail(user.getEmail());
    }

    public String removeItem(String token, Long id) {
        UserDto user = userClient.getUserDetails(token);
        System.out.println(user.getEmail());
        CartItem item = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getUserEmail().equalsIgnoreCase(user.getEmail())) {
            throw new RuntimeException("Unauthorized to delete this item");
        }

        cartRepository.deleteById(id);
        return "Item removed successfully.";
    }

    public String clearCart(String email) {
        cartRepository.deleteByUserEmail(email);
        return "Cart cleared successfully.";
    }

    public CartItem increaseQuantity(String email, String productName) {
        CartItem item = cartRepository.findByUserEmailAndProductName(email, productName)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(item.getQuantity() + 1);
        return cartRepository.save(item);
    }

    public CartItem decreaseQuantity(String email, String productName) {
        CartItem item = cartRepository.findByUserEmailAndProductName(email, productName)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            return cartRepository.save(item);
        }
        return item;
    }

    @Transactional
    public String checkout(String email) {
//        UserDto user = userClient.getUserDetails(token);
        cartRepository.deleteByUserEmail(email);
        // For now, we'll just clear the cart after checkout
        cartRepository.deleteByUserEmail(email);
        return "checkout successfully";
    }
}
