package com.shoppingcartservice.sachin.Controllers;

import com.shoppingcartservice.sachin.Services.JwtHelper;
import com.shoppingcartservice.sachin.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order/{userId}")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/createOrder")
    public ResponseEntity<?> createOrder(@PathVariable("userId") Integer userId, HttpServletRequest request) {
        if(!jwtHelper.getUserIdAuthenticated(userId,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Don't interfere into other's business...");
        return orderService.createOrder(userId);
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(@PathVariable("userId") Integer userId,HttpServletRequest request) {
        if(!jwtHelper.getUserIdAuthenticated(userId,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Don't interfere into other's business...");
        return orderService.getOrders(userId);
    }
}