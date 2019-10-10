package com.shoppingcartservice.sachin.Controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shoppingcartservice.sachin.Services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServices productServices;

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ObjectNode productDetails) {
        return productServices.addProduct(productDetails);
    }

    @PostMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestBody ObjectNode productDetails) {
        return productServices.updateProduct(productDetails);
    }

    @GetMapping("/getById/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Integer productId) {
        return productServices.getProductById(productId);
    }


    @GetMapping("/{category}")
    public ResponseEntity<?> getProductByCategory(@PathVariable("category") String category) {
        return productServices.getProductByCategory(category);
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<?> getProductBySearchString(@PathVariable("searchString") String searchString) {
        return productServices.getProductBySearchString(searchString);
    }
}
