package com.shoppingcartservice.sachin.Config;

import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.User.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DbInit implements CommandLineRunner {
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserProfileRepo userProfileRepo;

    @Override
    public void run(String... args) {
        UserCredentials adminCreds=userCredentialsRepo.findByEmail("admin@gmail.com");
        if(adminCreds==null){
            UserProfile admin=new UserProfile("admin","admin@gmail.com", 1112223334);
            userProfileRepo.save(admin);
            adminCreds=new UserCredentials();
            adminCreds.setUserProfile(admin);
            adminCreds.setEmail("admin@gmail.com");
            adminCreds.setPassword(passwordEncoder.encode(System.getenv("ADMIN_PASSWORD")));
            adminCreds.setRoles("ADMIN");
            userCredentialsRepo.save(adminCreds);
        } else {
            adminCreds.setPassword(passwordEncoder.encode(System.getenv("ADMIN_PASSWORD")));
        }
    }
}
