package com.shoppingcartservice.sachin.Reposistories.Order;

import com.shoppingcartservice.sachin.Entities.Order.Orders;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Orders,Long> {
    Orders findOrderByOrderId(Long orderId);
    List<Orders> findOrderByUserProfileOrderByOrderIdDesc(UserProfile userProfile);
}
