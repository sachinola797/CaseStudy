package com.shoppingcartservice.sachin.Controllers;

import com.shoppingcartservice.sachin.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/order/{userId}/createOrder")
    public ResponseEntity<?> createOrder(@PathVariable("userId") Integer userId) {
        return orderService.createOrder(userId);
    }

    @GetMapping("/order/{userId}/getOrders")
    public ResponseEntity<?> getOrders(@PathVariable("userId") Integer userId) {
        return orderService.getOrders(userId);
    }
}