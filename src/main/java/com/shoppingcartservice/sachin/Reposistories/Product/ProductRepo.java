package com.shoppingcartservice.sachin.Reposistories.Product;

import com.shoppingcartservice.sachin.Entities.Product.Category;
import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.Product.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    Product findProductByProductId(Long addressId);
    //Product findProductByNameAndDetailsAndPriceAndProductCategoryAndProductSubcategories(String name, String details, double price, ProductCategory productCategory, List<ProductSubcategory> productSubcategories);

    List<Product> findProductsBySubcategoriesContains(Subcategory subcategory);
    List<Product> findProductsByCategoryIgnoreCase(String category);

    List<Product> findAllByNameContainingOrDetailsContaining(String search,String search1);

    //considerable method
//    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:search%")
//    List<Product> find(@Param("search") String searchString);
}
