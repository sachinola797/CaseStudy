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
        // Delete all
       // this.userCredentialsRepo.deleteAll();

        // Crete users
//        UserCredentials dan = new UserCredentials("dan",passwordEncoder.encode("dan123"),"USER");
//        UserCredentials admin = new UserCredentials("admin",passwordEncoder.encode("admin123"),"ADMIN");
//        UserCredentials manager = new UserCredentials("manager",passwordEncoder.encode("manager123"),"MANAGER");
//
//        List<UserCredentials> users = Arrays.asList(dan,admin,manager);
//        UserCredentials userCredentials=userCredentialsRepo.findByUserId(3);
//        UserProfile admin= new UserProfile();
//        admin.setName("admin");
//        userProfileRepo.save(admin);
//        userCredentials.setUserProfile(admin);
//        userCredentialsRepo.save(userCredentials);
//        // Save to db
//        this.userCredentialsRepo.save(admin);
    }
}