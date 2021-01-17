package com.shoppingcartservice.sachin.Controllers;

import com.shoppingcartservice.sachin.DTOs.FilterRequestDTO;
import com.shoppingcartservice.sachin.DTOs.ProductDTO;
import com.shoppingcartservice.sachin.Services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        int verify=productDTO.isNullEntriesPresent();
        if(verify ==1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more fields are empty...");

        return productServices.addProduct(productDTO);
    }

    @PostMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO) {
        int verify=productDTO.isNullEntriesPresent();
        if(verify==1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more fields are empty...");
        if(verify==2)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please Provide Product Id...");
        return productServices.updateProduct(productDTO);
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {

        return productServices.getProductById(productId);
    }


    @GetMapping("/getByCategory/{category}")
    public ResponseEntity<?> getProductByCategory(@PathVariable("category") String category) {
        return productServices.getProductByCategory(category);
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<?> getProductBySearchString(@PathVariable("searchString") String searchString) {
        return productServices.getProductBySearchString(searchString);
    }

    @PostMapping("{category}/getFilteredProducts")
    public ResponseEntity<?> getFilteredProducts(@PathVariable("category") String category, @RequestBody FilterRequestDTO filterRequestDTO){
        if(filterRequestDTO.validatePriceFilters())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter valid price...");

        filterRequestDTO.setCategory(category);
        return productServices.getFilteredProducts(filterRequestDTO);
    }

    @GetMapping("/allCategories")
    public ResponseEntity<?> getAllCategories() {
        return productServices.getAllCategories();
    }

    @GetMapping("/allProducts")
    public ResponseEntity<?> getAllProducts() {
        return productServices.getAllProducts();
    }

}
