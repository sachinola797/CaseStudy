package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.ProductSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryRepo extends JpaRepository<ProductSubcategory,Integer> {
    ProductSubcategory getProductSubcategoryBySubcategoryId(Integer subcategoryId);
    ProductSubcategory getProductSubcategoryByName(String name);
}
