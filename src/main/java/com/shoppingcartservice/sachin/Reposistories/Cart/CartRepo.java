package com.shoppingcartservice.sachin.Reposistories.Cart;

import com.shoppingcartservice.sachin.Entities.Cart.Cart;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {
    Cart findCartByCartId(Long cartId);
    Cart findCartByUserProfile(UserProfile userProfile);
}

