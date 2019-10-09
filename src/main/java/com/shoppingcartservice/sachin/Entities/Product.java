package com.shoppingcartservice.sachin.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String name;
    private double price;
    private String details;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private ProductCategory productCategory;

    @ManyToMany
    @JoinColumn(name="subcategoryId")
    private List<ProductSubcategory> productSubcategories;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
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

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public List<ProductSubcategory> getProductSubcategories() {
        return productSubcategories;
    }

    public void setProductSubcategories(List<ProductSubcategory> productSubcategories) {
        this.productSubcategories = productSubcategories;
    }

}
