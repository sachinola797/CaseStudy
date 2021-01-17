package com.shoppingcartservice.sachin.Reposistories.Product;

import com.shoppingcartservice.sachin.Entities.Product.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    Category getCategoryByCategoryId(Long categoryId);
    Category getCategoryByNameIgnoreCase(String name);
}

