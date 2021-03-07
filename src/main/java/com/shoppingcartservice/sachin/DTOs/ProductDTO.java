package com.shoppingcartservice.sachin.DTOs;

import java.util.List;

public class ProductDTO {
    private long productId;
    private String name;
    private double price;
    private String details;
    private String category;
    private List<String> subcategories;

    public int isNullEntriesPresent(){
        if(this.name==null|| this.details==null||this.category==null||this.subcategories==null||getName().isEmpty()|| getPrice()==0 || getDetails().isEmpty() ||  getCategory().isEmpty() ||  getSubcategories().isEmpty())
            return 1;
        if(productId==0)
            return 2;
        return 0;
    }

    public ProductDTO(String name, double price, String details, String category, List<String> subcategories) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.category = category;
        this.subcategories = subcategories;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

}
