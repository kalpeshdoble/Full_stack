package com.example.Order_Service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import com.order.orderservice.dto.Items;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long id;
    private String userEmail;
    private String productName;
    private int quantity;
    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public CartItem(Long id, double price, String productName, int quantity, String userEmail) {
        this.id = id;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
        this.userEmail = userEmail;
    }
}
