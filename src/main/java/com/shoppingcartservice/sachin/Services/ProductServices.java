package com.shoppingcartservice.sachin.Services;



import com.shoppingcartservice.sachin.DTOs.ProductDTO;
import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.Product.Category;
import com.shoppingcartservice.sachin.Entities.Product.Subcategory;

import com.shoppingcartservice.sachin.Reposistories.Product.CategoryRepo;
import com.shoppingcartservice.sachin.Reposistories.Product.ProductRepo;
import com.shoppingcartservice.sachin.Reposistories.Product.SubcategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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


    public ResponseEntity<?> addProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDetails(productDTO.getDetails());
        product.setPrice(productDTO.getPrice());
        List<Subcategory> p_subcategories =new ArrayList<>();

           Category category = categoryRepo.getCategoryByNameIgnoreCase(productDTO.getCategory());
           List<Subcategory> c_subcategories;
           if (category == null) {
               category = new Category();
               category.setName(productDTO.getCategory());
               c_subcategories = new ArrayList<>();
           }
           else
               c_subcategories = category.getSubcategories();
           List<Subcategory> forRollBack=new ArrayList<>();
           try{
               List<String> s_name=productDTO.getSubcategories();
               for(String name :s_name){
                   Subcategory subcategory = subcategoryRepo.getSubcategoryByName(name);
                   if (subcategory != null && !c_subcategories.contains(subcategory))
                       throw new Exception("Subcategory \""+subcategory.getName() + "\" has been already mapped in different Category.");
                   else if(subcategory==null){
                       subcategory = new Subcategory();
                       subcategory.setName(name);
                       subcategoryRepo.save(subcategory);
                       forRollBack.add(subcategory);
                   }
                   p_subcategories.add(subcategory);
               }
           }catch (Exception e){
               subcategoryRepo.deleteAll(forRollBack);
               System.out.println(e);
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString().substring(20));
           }

            p_subcategories.forEach(newSubcategory ->{
                if(!c_subcategories.contains(newSubcategory))
                    c_subcategories.add(newSubcategory);
            });
           category.setSubcategories(c_subcategories);
           categoryRepo.save(category);
           product.setCategory(category.getName());
           product.setSubcategories(p_subcategories);
           productRepo.save(product);

        return ResponseEntity.ok().body(product);
    }

    public ResponseEntity<?> updateProduct(ProductDTO productDTO) {
        Product product=productRepo.findProductByProductId(productDTO.getProductId());

        if(product==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't exist!!!");

        product.setName(productDTO.getName());
        product.setDetails(productDTO.getDetails());
        product.setPrice(productDTO.getPrice());

        List<Subcategory> c_subcategories;
        Category category = categoryRepo.getCategoryByNameIgnoreCase(productDTO.getCategory());
        if (category == null) {
            category = new Category();
            category.setName(productDTO.getCategory());
            c_subcategories=new ArrayList<>();
        }
        else
            c_subcategories=category.getSubcategories();


        List<Subcategory> pastSubcategories=product.getSubcategories();
        List<Subcategory> p_subcategories=new ArrayList<>();
        List<Subcategory> forRollBack=new ArrayList<>();
        try {
            List<String> s_name=productDTO.getSubcategories();
            for(String name :s_name){
                Subcategory subcategory = subcategoryRepo.getSubcategoryByName(name);
                if (subcategory != null && !c_subcategories.contains(subcategory)) {
                    throw new Exception("Subcategory \""+subcategory.getName() + "\" has been already mapped in different Category.");
                }
                else if(subcategory==null) {
                    subcategory = new Subcategory();
                    subcategory.setName(name);
                    subcategoryRepo.save(subcategory);
                    forRollBack.add(subcategory);
                }
                if(pastSubcategories.contains(subcategory))
                    pastSubcategories.remove(subcategory);
                p_subcategories.add(subcategory);
            }
        }catch (Exception e){
            System.out.println(e);
            subcategoryRepo.deleteAll(forRollBack);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString().substring(20));
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
        product.setCategory(category.getName());
        productRepo.save(product);

        category.setSubcategories(c_subcategories);
        categoryRepo.save(category);

        return ResponseEntity.ok().body(product);
    }

    public ResponseEntity<?> getProductById(Integer productId) {
        Product product=productRepo.findProductByProductId(productId);
        if(product==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't exists by this product id!!!");

        return ResponseEntity.ok(product);
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

        return ResponseEntity.ok().body(products);
    }

    public ResponseEntity<?> getProductBySearchString(String searchString) {

        return ResponseEntity.ok(productRepo.find(searchString.toLowerCase()));
    }

    public ResponseEntity<?> getAllCategories() {

        return ResponseEntity.ok(categoryRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
    }

//    public ResponseEntity<?> getFilteredProductByCategory(String category,ObjectNode filters){
//
//        return null;
//    }


}
