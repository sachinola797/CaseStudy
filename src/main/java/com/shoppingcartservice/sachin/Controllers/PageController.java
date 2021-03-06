package com.shoppingcartservice.sachin.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/loginPage")
    public String login() {
        return "loginPage.html";
    }

    @GetMapping("/")
    public String home(){ return "homePage.html";}

    @GetMapping("/addProductPage")
    public String addProduct(){ return "productPage.html";}

    @GetMapping("/modifyProductPage")
    public String modifyProduct(){ return "productPage.html";}

    @GetMapping("/customerSupportPage")
    public String customerSupportPage(){ return "customerSupportPage.html";}

    @GetMapping("/userProfilePage")
    public String userProfilePage(){ return "userProfilePage.html";}

    @GetMapping("/cartPage")
    public String cartPage(){ return "cartPage.html";}

    @GetMapping("/orderPage")
    public String orderPage(){ return "orderPage.html";}
}
