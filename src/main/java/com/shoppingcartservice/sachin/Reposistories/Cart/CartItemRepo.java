package com.shoppingcartservice.sachin.Reposistories.Cart;

import com.shoppingcartservice.sachin.Entities.Cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    CartItem getCartItemByCartItemId(Long cartItemId);
    //List<CartItem> getCartItemByProduct(Product product);
}
