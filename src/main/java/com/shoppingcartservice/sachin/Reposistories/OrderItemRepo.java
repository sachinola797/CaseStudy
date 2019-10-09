package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.OrderItem;
import com.shoppingcartservice.sachin.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {
    OrderItem getCartItemByOrderItemId(Integer orderItemId);
    //List<OrderItem> getCartItemByProduct(Product product);
}
