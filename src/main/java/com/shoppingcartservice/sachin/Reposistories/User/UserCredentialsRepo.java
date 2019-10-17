package com.shoppingcartservice.sachin.Reposistories.User;

import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserCredentialsRepo extends JpaRepository<UserCredentials,String> {
    UserCredentials findByEmail(String email);
    UserCredentials findByUserProfile(UserProfile userProfile);
    UserCredentials findByUserId(long userId);
}
