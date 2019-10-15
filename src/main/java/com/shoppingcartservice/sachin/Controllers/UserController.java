package com.shoppingcartservice.sachin.Controllers;

import com.shoppingcartservice.sachin.DTOs.UserProfileDTO;

import com.shoppingcartservice.sachin.Services.JwtHelper;
import com.shoppingcartservice.sachin.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;


@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtHelper jwtHelper;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(UserCredentials user) {
//        return userService.login(user);
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@FormParam("name") String name,@FormParam("email") String email,@FormParam("password") String password) {
        if(name==null || email==null || password==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more fields are empty");

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
    public ResponseEntity<?> getProfile(@PathVariable("userID") Integer userID,HttpServletRequest request){
        if(!jwtHelper.getUserIdAuthenticated(userID,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not allowed to see this profile...");
        return userService.getProfile(userID);
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(UserProfileDTO userProfileDTO,HttpServletRequest request){
        if(userProfileDTO.isNullEntriesPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more fields are empty.");
        if(!jwtHelper.getUserIdAuthenticated(userProfileDTO.getUserID(),request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please update your own profile...");

        return userService.updateProfile(userProfileDTO);
    }

}
