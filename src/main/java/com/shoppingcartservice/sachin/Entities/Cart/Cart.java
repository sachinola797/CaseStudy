package com.shoppingcartservice.sachin.Entities.Cart;

import com.shoppingcartservice.sachin.Entities.User.UserProfile;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartId;
    @OneToMany
    @JoinColumn(name="cartItems")
    private List<CartItem> cartItems;
    @OneToOne
    private UserProfile userProfile;
    public long getCartId() {
        return cartId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
}
