package com.shoppingcartservice.sachin.Entities.Cart;

import com.shoppingcartservice.sachin.Entities.Product.Product;

import javax.persistence.*;

@Entity
public class CartItem {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cartItemId;
    private int quantity;
    @ManyToOne
    private Product product;

    public long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
