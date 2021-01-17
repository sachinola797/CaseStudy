package com.shoppingcartservice.sachin.Entities.Product;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;
    private String name;
    private double price;
    private String details;
    private String category;

    @ManyToMany
    private List<Subcategory> subcategories;



    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
