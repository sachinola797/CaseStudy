package com.shoppingcartservice.sachin.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    @OneToMany
    @JoinColumn(name="cartItems")
    private List<CartItem> cartItems;
    @OneToOne
    private UserProfile userProfile;
    public int getCartId() {
        return cartId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
