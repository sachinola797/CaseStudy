package com.shoppingcartservice.sachin.Config;

import com.shoppingcartservice.sachin.DTOs.ProductDTO;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.User.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            UserProfile admin=new UserProfile("admin","admin@gmail.com");
            userProfileRepo.save(admin);
            adminCreds=new UserCredentials();
            adminCreds.setUserProfile(admin);
            adminCreds.setEmail("admin@gmail.com");
            adminCreds.setPassword(passwordEncoder.encode("admin123"));
            adminCreds.setRoles("ADMIN");
            userCredentialsRepo.save(adminCreds);
        }
    }
}