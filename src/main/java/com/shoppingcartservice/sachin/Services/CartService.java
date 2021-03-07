package com.shoppingcartservice.sachin.Services;

import com.shoppingcartservice.sachin.Entities.Cart.Cart;
import com.shoppingcartservice.sachin.Entities.Cart.CartItem;
import com.shoppingcartservice.sachin.Entities.Product.Product;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.Cart.CartItemRepo;
import com.shoppingcartservice.sachin.Reposistories.Cart.CartRepo;
import com.shoppingcartservice.sachin.Reposistories.Product.ProductRepo;
import com.shoppingcartservice.sachin.Reposistories.User.UserProfileRepo;
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
        Cart cart=cartRepo.findCartByUserProfile(user);
        if(cart==null){
            cart= new Cart();
            cart.setUserProfile(user);
            cartRepo.save(cart);
        }
        if(cart.getCartItems()==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your Cart is Empty!!!");

        return ResponseEntity.ok(cart);
    }

    public ResponseEntity<?> addProductToCart(Integer userId,Integer productId) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        Product product=productRepo.findProductByProductId(productId);
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

        if(product==null)
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

    public ResponseEntity<?> changeQuantity1(Integer userId,Integer productId, Integer quantity) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        Product product=productRepo.findProductByProductId(productId);
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
                    if(quantity<1) {
                        cartItemRepo.delete(cartItem);
                        return ResponseEntity.ok(product.getName()+" removed successfully!!!");
                    }
                    else
                        cartItem.setQuantity(quantity);
                    cartItemRepo.save(cartItem);
                    cartRepo.save(cart);
                    return ResponseEntity.ok(cartItem);
                }
            }
        }
        else
            cartItems=new ArrayList<>();
        if(quantity>0) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepo.save(cartItem);
            cartItems.add(cartItem);
            cart.setCartItems(cartItems);
            cartRepo.save(cart);
            return ResponseEntity.ok(cartItem);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is not available in the cart!!!");
    }

    public ResponseEntity<?> changeQuantity2(Integer userId,Integer cartItemId, Integer quantity) {
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        CartItem cartItem=cartItemRepo.getCartItemByCartItemId(cartItemId);
        if(cartItem==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CartItem doesn't Exist!!!");
        Cart cart=cartRepo.findCartByUserProfile(user);
        if(cart==null){
            cart= new Cart();
            cart.setUserProfile(user);
        }

        List<CartItem> cartItems=cart.getCartItems();

        if(cartItems!=null && cartItems.contains(cartItem)) {
            if (quantity <1) {
                cartItemRepo.delete(cartItem);
                return ResponseEntity.ok(cartItem.getProduct().getName() + " removed successfully!!!");
            } else
                cartItem.setQuantity(quantity);
            cartItemRepo.save(cartItem);
            cartRepo.save(cart);
            return ResponseEntity.ok(cartItem);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This cart item doesn't belongs to you...");
    }


}
