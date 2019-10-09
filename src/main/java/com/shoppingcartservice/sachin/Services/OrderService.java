package com.shoppingcartservice.sachin.Services;

import com.shoppingcartservice.sachin.Entities.Cart.Cart;
import com.shoppingcartservice.sachin.Entities.Cart.CartItem;
import com.shoppingcartservice.sachin.Entities.Order.OrderItem;
import com.shoppingcartservice.sachin.Entities.Order.Orders;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;

    public ResponseEntity<?> createOrder(Integer userId) {
        UserProfile user = userProfileRepo.getUserProfileByUserID(userId);
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't Exist!!!");

        Cart cart = cartRepo.findCartByUserProfile(user);
        if (cart == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your cart is Empty!!!");

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your cart is Empty!!!");
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItemRepo.save(orderItem);
            orderItems.add(orderItem);
            cartItemRepo.delete(cartItem);
        }
        Orders order = new Orders();
        order.setOrderItems(orderItems);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("time :" + dtf.format(now));
        order.setOrderStatus("Order successfully Placed on " + dtf.format(now));
        order.setUserProfile(user);
        orderRepo.save(order);
        cartRepo.delete(cart);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(order);
    }

    public ResponseEntity<?> getOrders(Integer userId) {
        UserProfile user = userProfileRepo.getUserProfileByUserID(userId);
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't Exist!!!");
        List<Orders> orders=orderRepo.findOrderByUserProfileOrderByOrderIdDesc(user);
        if(orders.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You haven't ordered anything!!!");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(orders);
    }
}