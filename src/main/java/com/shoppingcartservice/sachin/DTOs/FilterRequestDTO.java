package com.shoppingcartservice.sachin.DTOs;

import java.util.List;

public class FilterRequestDTO {
    private String searchString;
    private List<String> subcategories;
    private String category;
    private double minPrice;
    private double maxPrice;

    public boolean validatePriceFilters(){
        if(maxPrice<0||minPrice<0)
            return true;
        return false;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
