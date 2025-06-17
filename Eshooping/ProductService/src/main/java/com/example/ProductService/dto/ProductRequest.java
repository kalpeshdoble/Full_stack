package com.example.ProductService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Name is required")  // Ensures the name is not blank
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters")  // Enforces name length constraints
    private String name;

    @NotBlank(message = "Description is required")  // Ensures the description is not blank
    @Size(min = 10, max = 500, message = "Description should be between 10 and 500 characters")  // Enforces description length constraints
    private String description;

    @NotBlank(message = "Category is required")  // Ensures the category is not blank
    private String category;

    @NotNull(message = "Price is required")  // Ensures the price is not null
    @Min(value = 0, message = "Price must be a positive value")  // Ensures price is greater than or equal to 0
    private double price;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")  // Ensures stock is non-negative
    private int stock;

    @NotBlank(message = "Merchant email is required")  // Ensures the merchant email is not blank
    @Email(message = "Invalid email format")  // Ensures the merchant email has a valid email format
    private String merchantEmail;

  private  String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
