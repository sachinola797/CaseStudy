package com.shoppingcartservice.sachin.Entities2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.FormParam;

@RestController
public class Controller {
    @Autowired
    private ProductService2 productService2;

    @GetMapping("/add/{a}/{p}")
    public ResponseEntity<?> add(@PathVariable("a") String cat,@PathVariable("p") String pcat){
        Category category=productService2.addCategory(cat,pcat);
        return ResponseEntity.ok(category);
    }
}
