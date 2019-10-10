package com.shoppingcartservice.sachin.Entities.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subcategoryId;
    private String name;



    public Subcategory() {
    }

    public Subcategory(String name) {
        this.name = name;
    }

    public int getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(int subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}