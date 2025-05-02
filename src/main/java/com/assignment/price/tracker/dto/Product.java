package com.assignment.price.tracker.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Product {
    private String productUrl;
    private Double price;

    public String getProductUrl() {
        return productUrl;
    }

    public Double getPrice() {
        return price;
    }
}
