package com.shoppingcartservice.sachin.Entities2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo2 extends JpaRepository<Category,Integer> {
    Category getCategoryByCategoryId(Integer categoryId);
    Category getCategoryByNameIgnoreCase(String name);
    Category getCategoryByParentCategoryId(Integer parentCategoryId);
}
