package com.example.CartService.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    // Getters and setters (Lombok @Data already includes these, so optional to keep)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
