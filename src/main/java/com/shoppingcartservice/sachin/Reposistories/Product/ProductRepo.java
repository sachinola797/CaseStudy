package com.shoppingcartservice.sachin.Reposistories.Product;

import com.shoppingcartservice.sachin.Entities.Product.Category;
import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.Product.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    Product findProductByProductId(Integer addressId);
    //Product findProductByNameAndDetailsAndPriceAndProductCategoryAndProductSubcategories(String name, String details, double price, ProductCategory productCategory, List<ProductSubcategory> productSubcategories);
    List<Product> findProductsBySubcategoriesContains(Subcategory subcategory);
    List<Product> findByCategoryIgnoreCase(String category);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:search%")
    List<Product> find(@Param("search") String searchString);
}
