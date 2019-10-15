package com.shoppingcartservice.sachin.Reposistories.Order;

import com.shoppingcartservice.sachin.Entities.Order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> {
    OrderItem getCartItemByOrderItemId(Integer orderItemId);
    //List<OrderItem> getCartItemByProduct(Product product);
}
