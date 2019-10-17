package com.shoppingcartservice.sachin.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class page {
    @GetMapping("/")
    public String login() {
        return "loginPage.html";
    }

    @GetMapping("/addProductPage")
    public String addProduct(){ return "productPage.html";}

    @GetMapping("/modifyProductPage")
    public String modifyProduct(){ return "productPage.html";}
}
