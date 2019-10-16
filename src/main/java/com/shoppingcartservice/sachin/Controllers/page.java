package com.shoppingcartservice.sachin.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class page {
    @GetMapping("/")
    public String login() {
        return "loginPage.html";
    }
}
