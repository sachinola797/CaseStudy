package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<ProductCategory,Integer> {
    ProductCategory getProductCategoryByCategoryId(Integer categoryId);
    ProductCategory getProductCategoryByNameIgnoreCase(String name);
}

