package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.CartItem;
import com.shoppingcartservice.sachin.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
    CartItem getCartItemByCartItemId(Integer cartItemId);
    //List<CartItem> getCartItemByProduct(Product product);
}
