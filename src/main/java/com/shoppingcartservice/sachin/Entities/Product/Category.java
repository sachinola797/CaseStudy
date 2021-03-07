package com.shoppingcartservice.sachin.Entities.Product;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;
    private String name;

    @OneToMany
    private List<Subcategory> subcategories;


    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
