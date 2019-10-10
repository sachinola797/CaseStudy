package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.Product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Integer> {
    Category getCategoryByCategoryId(Integer categoryId);
    Category getCategoryByNameIgnoreCase(String name);
}

