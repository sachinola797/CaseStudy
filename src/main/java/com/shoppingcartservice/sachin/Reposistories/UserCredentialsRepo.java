package com.shoppingcartservice.sachin.Reposistories;

import com.shoppingcartservice.sachin.Entities.UserCredentials;
import com.shoppingcartservice.sachin.Entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserCredentialsRepo extends JpaRepository<UserCredentials,String> {
    UserCredentials findByEmail(String email);
    UserCredentials findByUserProfile(UserProfile userProfile);
}
