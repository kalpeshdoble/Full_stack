package com.example.CartService.DTO;

import lombok.*;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class ProductDto {
    private String name;
    private String description;
private int stock;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ProductDto(String name, String description, String category, double price, int stock) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.stock=stock;
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




}
