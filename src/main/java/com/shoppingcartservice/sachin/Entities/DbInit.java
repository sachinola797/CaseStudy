package com.shoppingcartservice.sachin.Entities;

import com.shoppingcartservice.sachin.Reposistories.UserCredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        // Delete all
        this.userCredentialsRepo.deleteAll();

        // Crete users
        UserCredentials dan = new UserCredentials("dan",passwordEncoder.encode("dan123"),"USER");
        UserCredentials admin = new UserCredentials("admin",passwordEncoder.encode("admin123"),"ADMIN");
        UserCredentials manager = new UserCredentials("manager",passwordEncoder.encode("manager123"),"MANAGER");

        List<UserCredentials> users = Arrays.asList(dan,admin,manager);

        // Save to db
        this.userCredentialsRepo.saveAll(users);
    }
}