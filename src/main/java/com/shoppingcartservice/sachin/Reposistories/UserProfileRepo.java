package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile,Integer> {
    UserProfile getUserProfileByUserID(Integer userID);
   // UserProfile getUserProfileByAddress(Address address);
}