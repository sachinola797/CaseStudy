package com.shoppingcartservice.sachin.Entities2;

import com.shoppingcartservice.sachin.Reposistories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class ProductService2 {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo2 categoryRepo;


    @Autowired
    private EntityManager em;

    @Autowired
    private ProductService2 productService2;

//    public ResponseEntity<?> addProduct(ObjectNode productDetails) {
//        Product product = new Product();
//        product.setName(productDetails.get("name").asText());
//        product.setDetails(productDetails.get("details").asText());
//        product.setPrice(productDetails.get("price").asDouble());
//
//
//        ProductCategory productCategory = categoryRepo.getProductCategoryByNameIgnoreCase(productDetails.get("category").asText());
//        if (productCategory == null) {
//            productCategory = new ProductCategory();
//            productCategory.setName(productDetails.get("category").asText());
//            categoryRepo.save(productCategory);
//        }
//        product.setProductCategory(productCategory);
//
//        List<ProductSubcategory> subcategories = new ArrayList<>();
//        int i = 0;
//        while (productDetails.get("subcategory").get(i) != null) {
//            ProductSubcategory productSubcategory = subcategoryRepo.getProductSubcategoryByName(productDetails.get("subcategory").asText());
//            if (productSubcategory == null) {
//                productSubcategory = new ProductSubcategory();
//                productSubcategory.setName(productDetails.get("subcategory").asText());
//                subcategoryRepo.save(productSubcategory);
//            }
//            subcategories.add(productSubcategory);
//            i++;
//        }
//
//        product.setProductSubcategories(subcategories);
//
////        if(productRepo.findProductByNameAndDetailsAndPriceAndProductCategoryAndProductSubcategories(product.getName(),product.getDetails(),product.getPrice(),product.getProductCategory(),product.getProductSubcategories())!=null)
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product already exists!!!");
//        productRepo.save(product);
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
//    }

//    public ResponseEntity<?> updateProduct(ObjectNode productDetails) {
//        Product product=productRepo.findProductByProductId(productDetails.get("productId").asInt());
//        if(product==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't exist!!!");
//        product.setName(productDetails.get("name").asText());
//        product.setDetails(productDetails.get("details").asText());
//        product.setPrice(productDetails.get("price").asDouble());
//
//        ProductCategory productCategory = categoryRepo.getProductCategoryByNameIgnoreCase(productDetails.get("category").asText());
//        if (productCategory == null) {
//            productCategory = new ProductCategory();
//            productCategory.setName(productDetails.get("category").asText());
//            categoryRepo.save(productCategory);
//        }
//        product.setProductCategory(productCategory);
//
//
//        List<ProductSubcategory> productSubcategories = new ArrayList<>();
//        int i=0;
//        while (productDetails.get("subcategory").get(i)!= null) {
//            ProductSubcategory productSubcategory = subcategoryRepo.getProductSubcategoryByName(productDetails.get("subcategory").get(i).asText());
//            if (productSubcategory == null) {
//                productSubcategory = new ProductSubcategory();
//                productSubcategory.setName(productDetails.get("subcategory").get(i).asText());
//                subcategoryRepo.save(productSubcategory);
//            }
//            productSubcategories.add(productSubcategory);
//            i++;
//        }
//
//        product.setProductSubcategories(productSubcategories);
//        productRepo.save(product);
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
//    }

//    public ResponseEntity<?> getProduct1(Integer productId) {
//        Product product=productRepo.findProductByProductId(productId);
//        if(product==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't exists by the product id!!!");
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
//    }

//    public ResponseEntity<?> getProduct2(String category) {
//        ProductCategory productCategory=categoryRepo.getProductCategoryByNameIgnoreCase(category);
//        if (productCategory==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category doesn't exists!!!");
//
//        List<Product> product=productRepo.getProductsByProductCategory(productCategory);
//        if(product==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No product is assigned to this category!!!");
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
//    }

//    public ResponseEntity<?> getProductBySearchString(String searchString) {
//
//        return ResponseEntity.ok(productRepo.find(searchString.toLowerCase()));
//    }

    public Category addCategory(String categoryName,String parentCategoryName) {
        Category category=categoryRepo.getCategoryByNameIgnoreCase(categoryName);
        if(category!=null)
            return category;
        else{
            category=new Category();
        }

        category.setName(categoryName);
        if( parentCategoryName!=null){
            if(categoryRepo.getCategoryByNameIgnoreCase(parentCategoryName)==null ) {
                addCategory(parentCategoryName, null);
                category.setParentCategoryId(categoryRepo.getCategoryByNameIgnoreCase(parentCategoryName).getCategoryId());
            }else {
                category.setParentCategoryId(categoryRepo.getCategoryByNameIgnoreCase(parentCategoryName).getCategoryId());
            }
        }
        categoryRepo.save(category);
        return category;

    }
}
