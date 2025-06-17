package com.example.ProductService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
//@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
private String imgUrl;
    public Product(String name, String description, String category, double price, int stock, String merchantEmail,String imgUrl) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.merchantEmail = merchantEmail;
        this.imgUrl=imgUrl;
    }

    public Product(String category, String description, String name, double price,int stock) {
        this.category = category;
        this.description = description;
        this.name = name;
        this.price = price;
        this.stock=stock;
    }

    public Product() {
    }

    private String category;
    private double price;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    private int stock;
    private String merchantEmail;
}
