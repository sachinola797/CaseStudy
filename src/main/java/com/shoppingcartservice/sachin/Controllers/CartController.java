package com.shoppingcartservice.sachin.Controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shoppingcartservice.sachin.Services.CartService;
import com.shoppingcartservice.sachin.Services.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/{userId}/getCart")
    public ResponseEntity<?> getCart(@PathVariable("userId") Integer userId, HttpServletRequest request) {
        if(!jwtHelper.getUserIdAuthenticated(userId,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Don't interfere into other's business...");
        return cartService.getCart(userId);
    }

    @GetMapping("/{userId}/add/{productId}")
    public ResponseEntity<?> addProductToCart(@PathVariable("userId") Integer userId,@PathVariable("productId") Integer productId,HttpServletRequest request) {
        if(!jwtHelper.getUserIdAuthenticated(userId,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Don't interfere into other's business...");
        return cartService.addProductToCart(userId,productId);
    }

    @GetMapping("/{userId}/getCartItem/{cartItemId}")
    public ResponseEntity<?> getCartItem(@PathVariable("userId") Integer userId,@PathVariable("cartItemId") Integer cartItemId,HttpServletRequest request) {
        if(!jwtHelper.getUserIdAuthenticated(userId,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Don't interfere into other's business...");
        return cartService.getCartItem(userId,cartItemId);
    }

    @GetMapping("/{userId}/remove/{productId}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable("userId") Integer userId,@PathVariable("productId") Integer productId,HttpServletRequest request) {
        if(!jwtHelper.getUserIdAuthenticated(userId,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Don't interfere into other's business...");
        return cartService.removeProductFromCart(userId,productId);
    }


    @PostMapping("/{userId}/changeQuantity/{productId}")
    public ResponseEntity<?> changeQuantity1(@PathVariable("userId") Integer userId,@PathVariable("productId") Integer productId,@FormParam("quantity")  Integer quantity,HttpServletRequest request) {
        if(!jwtHelper.getUserIdAuthenticated(userId,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Don't interfere into other's business...");
//        if(quantity==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please insert quantity...");
        return cartService.changeQuantity1(userId,productId,quantity);
    }

    @PostMapping("/changeQuantity/{cartItemId}")
   public ResponseEntity<?> changeQuantity2(@PathVariable("cartItemId") Integer cartItemId,@FormParam("quantity") Integer quantity,HttpServletRequest request) {
//        if(quantity==null)
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please insert quantity...");
        return cartService.changeQuantity2(jwtHelper.getCurrentUserId(request),cartItemId,quantity);
   }

}





