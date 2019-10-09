package com.shoppingcartservice.sachin.Controllers;

import com.shoppingcartservice.sachin.Config.JwtTokenUtil;
import com.shoppingcartservice.sachin.Reposistories.AddressRepo;
import com.shoppingcartservice.sachin.Reposistories.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.UserProfileRepo;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.shoppingcartservice.sachin.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.FormParam;


@RestController
public class UserController {
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserService userService;


//    @PostMapping("/login")
//    public ResponseEntity<?> login(UserCredentials user) {
//        return userService.login(user);
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@FormParam("name") String name,@FormParam("email") String email,@FormParam("password") String password) {
        return userService.signup(name,email,password);
    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(UserCredentials user) {
//        UserCredentials currentUser=userCredentialsRepo.findByUserID(user.getUserID());
//        if(currentUser==null)
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User doesn't exist!!!");
//        else if(!jwtTokenUtil.validateToken(currentUser.getToken(),currentUser))
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User's Session Expired!!!");
//        else {
//            return ResponseEntity.ok("User Successfully logged out!!!");
//        }
//    }

    @GetMapping("/getProfile/{userID}")
    public ResponseEntity<?> getprofile(@PathVariable("userID") Integer userID){
        return userService.getProfile(userID);
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody ObjectNode details){
        return userService.updateProfile(details);
    }

}
