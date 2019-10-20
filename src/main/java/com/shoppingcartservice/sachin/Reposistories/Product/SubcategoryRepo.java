package com.shoppingcartservice.sachin.Reposistories.Product;

import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.Product.Subcategory;
import org.hibernate.validator.constraints.EAN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoryRepo extends JpaRepository<Subcategory,Integer> {
    Subcategory getSubcategoryBySubcategoryId(Integer subcategoryId);
    Subcategory getSubcategoryByNameIgnoreCase(String name);
}
