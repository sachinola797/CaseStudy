package com.shoppingcartservice.sachin.Reposistories.Product;

import com.shoppingcartservice.sachin.Entities.Product.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryRepo extends JpaRepository<Subcategory,Long> {
    Subcategory getSubcategoryBySubcategoryId(Long subcategoryId);
    Subcategory getSubcategoryByNameIgnoreCase(String name);
}
