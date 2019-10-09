package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.Cart;
import com.shoppingcartservice.sachin.Entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart,Integer> {
    Cart findCartByCartId(Integer cartId);
    Cart findCartByUserProfile(UserProfile userProfile);
}

