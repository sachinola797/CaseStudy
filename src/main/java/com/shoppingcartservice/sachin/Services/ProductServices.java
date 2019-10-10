package com.shoppingcartservice.sachin.Services;



import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.Product.Category;
import com.shoppingcartservice.sachin.Entities.Product.Subcategory;
import com.shoppingcartservice.sachin.Reposistories.*;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductServices {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private SubcategoryRepo subcategoryRepo;


    public ResponseEntity<?> addProduct(ObjectNode productDetails) {
        Product product = new Product();
        product.setName(productDetails.get("name").asText());
        product.setDetails(productDetails.get("details").asText());
        product.setPrice(productDetails.get("price").asDouble());
        List<Subcategory> p_subcategories =new ArrayList<>();

           Category category = categoryRepo.getCategoryByNameIgnoreCase(productDetails.get("category").asText());
           List<Subcategory> c_subcategories;
           if (category == null) {
               category = new Category();
               category.setName(productDetails.get("category").asText());
               c_subcategories = new ArrayList<>();
           }
           else
               c_subcategories = category.getSubcategories();
           List<Subcategory> forRollBack=new ArrayList<>();
           try{
               int i = 0;
               while (productDetails.get("subcategory").get(i) != null) {
                   Subcategory subcategory = subcategoryRepo.getSubcategoryByName(productDetails.get("subcategory").get(i).asText());
                   if (subcategory != null && !c_subcategories.contains(subcategory))
                       throw new Exception(subcategory.getName()+" already mapped in different Category.");
                   else if(subcategory==null){
                       subcategory = new Subcategory();
                       subcategory.setName(productDetails.get("subcategory").get(i).asText());
                       subcategoryRepo.save(subcategory);
                       forRollBack.add(subcategory);
                   }
                   p_subcategories.add(subcategory);i++;
               }
           }catch (Exception e){
               subcategoryRepo.deleteAll(forRollBack);
               System.out.println(e);
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more Subcategories are assigned to another Category");
           }

            p_subcategories.forEach(newSubcategory ->{
                if(!c_subcategories.contains(newSubcategory))
                    c_subcategories.add(newSubcategory);
            });
           category.setSubcategories(c_subcategories);
           categoryRepo.save(category);
           product.setSubcategories(p_subcategories);
           productRepo.save(product);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
    }

    public ResponseEntity<?> updateProduct(ObjectNode productDetails) {
        Product product=productRepo.findProductByProductId(productDetails.get("productId").asInt());

        if(product==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't exist!!!");

        product.setName(productDetails.get("name").asText());
        product.setDetails(productDetails.get("details").asText());
        product.setPrice(productDetails.get("price").asDouble());

        List<Subcategory> c_subcategories;
        Category category = categoryRepo.getCategoryByNameIgnoreCase(productDetails.get("category").asText());
        if (category == null) {
            category = new Category();
            category.setName(productDetails.get("category").asText());
            c_subcategories=new ArrayList<>();
        }
        else
            c_subcategories=category.getSubcategories();


        List<Subcategory> pastSubcategories=product.getSubcategories();
        List<Subcategory> p_subcategories=new ArrayList<>();
        List<Subcategory> forRollBack=new ArrayList<>();
        try {
            int i = 0;
            while (productDetails.get("subcategory").get(i) != null) {
                Subcategory subcategory = subcategoryRepo.getSubcategoryByName(productDetails.get("subcategory").get(i).asText());
                if (subcategory != null && !c_subcategories.contains(subcategory)) {
                    throw new Exception(subcategory.getName() + " already mapped in different Category.");
                }
                else if(subcategory==null) {
                    subcategory = new Subcategory();
                    subcategory.setName(productDetails.get("subcategory").get(i).asText());
                    subcategoryRepo.save(subcategory);
                    forRollBack.add(subcategory);
                }
                if(pastSubcategories.contains(subcategory))
                    pastSubcategories.remove(subcategory);
                p_subcategories.add(subcategory);
                i++;
            }
        }catch (Exception e){
            System.out.println(e);
            subcategoryRepo.deleteAll(forRollBack);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more Subcategories are assigned to another Category");
        }

        p_subcategories.forEach(newSubcategory ->{
            if(!c_subcategories.contains(newSubcategory))
                c_subcategories.add(newSubcategory);
        });

        pastSubcategories.forEach(subcategory -> {
            if(productRepo.findProductsBySubcategoriesContains(subcategory).size()<2){
                c_subcategories.remove(subcategory);
                subcategoryRepo.delete(subcategory);

            }
        });

        product.setSubcategories(p_subcategories);
        productRepo.save(product);

        category.setSubcategories(c_subcategories);
        categoryRepo.save(category);


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
    }

    public ResponseEntity<?> getProductById(Integer productId) {
        Product product=productRepo.findProductByProductId(productId);
        if(product==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't exists by the product id!!!");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
    }

    public ResponseEntity<?> getProductByCategory(String category) {
        Category productCategory=categoryRepo.getCategoryByNameIgnoreCase(category);
        if (productCategory==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category doesn't exists!!!");

        Set<Product> products=new HashSet<>();
        List<Subcategory>subcategories=productCategory.getSubcategories();
        if(subcategories.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category doesn't contain any subcategories!!!");
        subcategories.forEach(subcategory -> products.addAll(productRepo.findProductsBySubcategoriesContains(subcategory)));

        if(products==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No product is assigned to this category!!!");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(products);
    }

    public ResponseEntity<?> getProductBySearchString(String searchString) {

        return ResponseEntity.ok(productRepo.find(searchString.toLowerCase()));
    }

//    public ResponseEntity<?> getFilteredProductByCategory(String category,ObjectNode filters){
//
//        return null;
//    }


}
