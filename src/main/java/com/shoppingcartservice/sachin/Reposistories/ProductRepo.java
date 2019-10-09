package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.Product;
import com.shoppingcartservice.sachin.Entities.ProductCategory;
import com.shoppingcartservice.sachin.Entities.ProductSubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Integer> {
    Product findProductByProductId(Integer addressId);
    //Product findProductByNameAndDetailsAndPriceAndProductCategoryAndProductSubcategories(String name, String details, double price, ProductCategory productCategory, List<ProductSubcategory> productSubcategories);
    List<Product> getProductsByProductCategory(ProductCategory productCategory);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:search%")
    List<Product> find(@Param("search") String searchString);
}
