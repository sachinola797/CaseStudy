package com.shoppingcartservice.sachin.Reposistories.User;

import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile,Long> {
    UserProfile getUserProfileByUserID(Long userID);
    UserProfile getUserProfileByPhone(long phone);
}
