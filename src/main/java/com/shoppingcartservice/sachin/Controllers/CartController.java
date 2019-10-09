package com.shoppingcartservice.sachin.Controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shoppingcartservice.sachin.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;

@RestController
//@RequestMapping("/cart/{userId}")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart/{userId}/getCart")
    public ResponseEntity<?> getCart(@PathVariable("userId") Integer userId) {
        return cartService.getCart(userId);
    }

    @GetMapping("/cart/{userId}/add/{productId}")
    public ResponseEntity<?> addProductToCart(@PathVariable("userId") Integer userId,@PathVariable("productId") Integer productId) {
        return cartService.addProductToCart(userId,productId);
    }

    @GetMapping("/cart/{userId}/getCartItem/{cartItemId}")
    public ResponseEntity<?> getCartItem(@PathVariable("userId") Integer userId,@PathVariable("cartItemId") Integer cartItemId) {
        return cartService.getCartItem(userId,cartItemId);
    }
    //comment this method during debugging
    @GetMapping("/cart/{userId}/remove/{productId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable("userId") Integer userId,@PathVariable("productId") Integer productId) {
        return cartService.removeProductFromCart(userId,productId);
    }

    //enter 0 to remove decrease and 1 to increase the quantity
    @GetMapping("/cart/{userId}/changeQuantity/{productId}/{zeroOrOne}")
    public ResponseEntity<?> changeQuantity(@PathVariable("userId") Integer userId,@PathVariable("productId") Integer productId,@PathVariable("zeroOrOne") Integer operation) {
        return cartService.changeQuantity(userId,productId,operation);
    }


}





