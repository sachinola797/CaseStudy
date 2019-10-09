package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.Orders;
import com.shoppingcartservice.sachin.Entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Orders,Integer> {
    Orders findOrderByOrderId(Integer orderId);
    List<Orders> findOrderByUserProfileOrderByOrderIdDesc(UserProfile userProfile);
}