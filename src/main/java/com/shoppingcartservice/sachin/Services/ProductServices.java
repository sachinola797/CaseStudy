package com.shoppingcartservice.sachin.Services;



import com.shoppingcartservice.sachin.DTOs.FilterRequestDTO;
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
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;

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
        product.setName(capitalise(productDTO.getName()));
        product.setDetails(productDTO.getDetails());
        product.setPrice(productDTO.getPrice());
        List<Subcategory> p_subcategories =new ArrayList<>();

           Category category = categoryRepo.getCategoryByNameIgnoreCase(productDTO.getCategory());
           List<Subcategory> c_subcategories;
           if (category == null) {
               category = new Category();
               category.setName(capitalise(productDTO.getCategory()));
               c_subcategories = new ArrayList<>();
           }
           else
               c_subcategories = category.getSubcategories();
           List<Subcategory> forRollBack=new ArrayList<>();
           try{
               List<String> s_name=productDTO.getSubcategories();
               for(String name :s_name){
                   Subcategory subcategory = subcategoryRepo.getSubcategoryByNameIgnoreCase(name);
                   if (subcategory != null && !c_subcategories.contains(subcategory))
                       throw new Exception("Subcategory \""+subcategory.getName() + "\" has been already mapped in different Category.");
                   else if(subcategory==null){
                       subcategory = new Subcategory();
                       subcategory.setName(capitalise(name));
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

        product.setName(capitalise(productDTO.getName()));
        product.setDetails(productDTO.getDetails());
        product.setPrice(productDTO.getPrice());

        List<Subcategory> c_subcategories;
        Category category = categoryRepo.getCategoryByNameIgnoreCase(productDTO.getCategory());
        if (category == null) {
            category = new Category();
            category.setName(capitalise(productDTO.getCategory()));
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
                Subcategory subcategory = subcategoryRepo.getSubcategoryByNameIgnoreCase(name);
                if (subcategory != null && !c_subcategories.contains(subcategory)) {
                    throw new Exception("Subcategory \""+subcategory.getName() + "\" has been already mapped in different Category.");
                }
                else if(subcategory==null) {
                    subcategory = new Subcategory();
                    subcategory.setName(capitalise(name));
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

        String pastCategoryName=product.getCategory();
        product.setCategory(category.getName());
        productRepo.save(product);

        category.setSubcategories(c_subcategories);
        categoryRepo.save(category);

        Category pastCategory=categoryRepo.getCategoryByNameIgnoreCase(pastCategoryName);
        if(pastCategory.getSubcategories().isEmpty())
            categoryRepo.delete(pastCategory);

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
        List<Product> products=productRepo.findProductsByCategoryIgnoreCase(category);
        if(products.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No product is assigned to this category!!!");

        // A good method...
//        Set<Product> products=new HashSet<>();
//        List<Subcategory>subcategories=productCategory.getSubcategories();
//        if(subcategories.isEmpty())
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category doesn't contain any subcategories!!!");
//        subcategories.forEach(subcategory -> products.addAll(productRepo.findProductsBySubcategoriesContains(subcategory)));
//
//        if(products==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No product is assigned to this category!!!");

        return ResponseEntity.ok().body(products);
    }

    public ResponseEntity<?> getProductBySearchString(String searchString) {

        return ResponseEntity.ok(productRepo.find(searchString.toLowerCase()));
    }

    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok().body(categoryRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
    }
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok().body(productRepo.findAll(new Sort(Sort.Direction.ASC, "name")));
    }


    public ResponseEntity<?> getFilteredProducts(FilterRequestDTO filterRequestDTO){

        if(!filterRequestDTO.getCategory().equals("all")) {
            Category productCategory = categoryRepo.getCategoryByNameIgnoreCase(filterRequestDTO.getCategory());
            if (productCategory == null)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category doesn't exists!!!");
            filterRequestDTO.setCategory(productCategory.getName());
        }
        List<Subcategory> subcategories=new ArrayList<>();
        if(filterRequestDTO.getSubcategories()!=null) {
            for (String subcat_name : filterRequestDTO.getSubcategories()) {
                Subcategory subcategory = subcategoryRepo.getSubcategoryByNameIgnoreCase(subcat_name);
                if (subcategory == null)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(subcat_name + " doesn't exists!!!");
                subcategories.add(subcategory);
            }
        }
        double minPrice=filterRequestDTO.getMinPrice();
        double maxPrice=filterRequestDTO.getMaxPrice();

        if(minPrice>maxPrice && maxPrice>0) {
            double a=minPrice;
            minPrice=maxPrice;
            maxPrice=a;
        }

        List<Product> products=productRepo.findAll(new FilterSearchSpecification(minPrice,maxPrice,filterRequestDTO.getCategory(),filterRequestDTO.getSearchString()));
        List<Product>finalProducts=new ArrayList<>();
        for (Product p:products){
            if(p.getSubcategories().containsAll(subcategories))
                finalProducts.add(p);
        }
        Collections.sort(finalProducts, (p1, p2) -> {
            String p1_name=p1.getName().toLowerCase();
            String p2_name=p2.getName().toLowerCase();
            return p1_name.compareTo(p2_name);
        });

        return ResponseEntity.ok().body(finalProducts);
    }

    private String capitalise(String s){
        String[] list=s.split(" ");
        StringBuilder finalString= new StringBuilder();
        for(String a:list) {
            if(a.length()>1)
                finalString.append(" "+a.substring(0, 1).toUpperCase()+a.substring(1).toLowerCase());
            else if(a.length()>0)
                finalString.append(" "+a.substring(0, 1).toUpperCase());
        }
        return  finalString.substring(1);
    }

}
