package com.shoppingcartservice.sachin.Services;

import com.shoppingcartservice.sachin.Entities.Cart.Cart;
import com.shoppingcartservice.sachin.Entities.Cart.CartItem;
import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.CartItemRepo;
import com.shoppingcartservice.sachin.Reposistories.CartRepo;
import com.shoppingcartservice.sachin.Reposistories.ProductRepo;
import com.shoppingcartservice.sachin.Reposistories.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private ProductRepo productRepo;

    public ResponseEntity<?> getCart(Integer userId) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't Exist!!!");
        Cart cart=cartRepo.findCartByUserProfile(user);
        if(cart==null){
            cart= new Cart();
            cart.setUserProfile(user);
            cartRepo.save(cart);
        }
        if(cart.getCartItems()==null)
            return ResponseEntity.ok("Your Cart is Empty!!!");

        return ResponseEntity.ok(cart);
    }

    public ResponseEntity<?> addProductToCart(Integer userId,Integer productId) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        Product product=productRepo.findProductByProductId(productId);
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't Exist!!!");
        if(product==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't Exist!!!");

        Cart cart=cartRepo.findCartByUserProfile(user);
        if(cart==null){
            cart= new Cart();
            cart.setUserProfile(user);
        }

        List<CartItem> cartItems=cart.getCartItems();

        if(cartItems!=null) {
            for(CartItem cartItem:cartItems){
                if (cartItem.getProduct().equals(product)) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartItemRepo.save(cartItem);
                    cartRepo.save(cart);
                    return ResponseEntity.ok(cartItem);
                }
            }
        }
        else
            cartItems=new ArrayList<>();

        CartItem cartItem=new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItemRepo.save(cartItem);
        cartItems.add(cartItem);
        cart.setCartItems(cartItems);

        cartRepo.save(cart);
        return ResponseEntity.ok(cartItem);
    }

    public ResponseEntity<?> getCartItem(Integer userId,Integer cartItemId) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        CartItem cartItem=cartItemRepo.getCartItemByCartItemId(cartItemId);
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't Exist!!!");
        if(cartItem==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cart Item doesn't Exist anymore!!!");
        Cart cart=cartRepo.findCartByUserProfile(user);
        if(cart==null)
            return getCart(userId);
        if(cart.getCartItems().contains(cartItem))
            return ResponseEntity.ok(cartItem);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sorry, this doesn't belongs to you!!!");
    }

    public ResponseEntity<?> removeProductFromCart(Integer userId,Integer productId) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        Product product=productRepo.findProductByProductId(productId);
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't Exist!!!");
        else if(product==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't Exist!!!");

        Cart cart=cartRepo.findCartByUserProfile(user);
        if(cart==null)
            return getCart(userId);

        for(CartItem cartItem:cart.getCartItems()){
            if (cartItem.getProduct().getProductId()==productId) {
                cartItemRepo.delete(cartItem);
                return ResponseEntity.ok(product.getName()+" removed successfully!!!");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sorry, your cart doesn't contains this item!!!");
    }

    public ResponseEntity<?> changeQuantity(Integer userId,Integer productId, Integer operation) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        Product product=productRepo.findProductByProductId(productId);
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't Exist!!!");
        if(product==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product doesn't Exist!!!");

        Cart cart=cartRepo.findCartByUserProfile(user);
        if(cart==null){
            cart= new Cart();
            cart.setUserProfile(user);
        }
        System.out.println("all good");
        List<CartItem> cartItems=cart.getCartItems();

        if(cartItems!=null) {
            System.out.println("into if 1");
            for(CartItem cartItem:cartItems){
                System.out.println("into for loop");
                if (cartItem.getProduct().equals(product)) {
                    if(cartItem.getQuantity()==1 && operation==0) {
                        cartItemRepo.delete(cartItem);
//                        cartRepo.save(cart);
                        return ResponseEntity.ok(product.getName()+" removed successfully!!!");
                    }
                    else if(operation==1)
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                    else if(cartItem.getQuantity()>1 && operation==0)
                        cartItem.setQuantity(cartItem.getQuantity() - 1);
                    cartItemRepo.save(cartItem);
                    cartRepo.save(cart);
                    return ResponseEntity.ok(cartItem);
                }
            }
        }
        else
            cartItems=new ArrayList<>();
        if(operation==1) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItemRepo.save(cartItem);
            cartItems.add(cartItem);
            cart.setCartItems(cartItems);

            cartRepo.save(cart);

            return ResponseEntity.ok(cartItem);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is not available in the cart!!!");
    }
}
