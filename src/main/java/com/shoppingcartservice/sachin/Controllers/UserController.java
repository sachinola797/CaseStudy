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


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@FormParam("name") String name,@FormParam("email") String email, @FormParam("phone") Long phone,@FormParam("password") String password) {
        if(name==null || email==null || password==null || phone==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more fields are empty");

        return userService.signup(name,email,password,phone);
    }


    @GetMapping("/getProfile/{userID}")
    public ResponseEntity<?> getProfile(@PathVariable("userID") Long userID,HttpServletRequest request){
        if(!jwtHelper.getUserIdAuthenticated(userID,request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not allowed to see this profile...");
        return userService.getProfile(userID);
    }

    @GetMapping("/getProfileByPhone/{phone}")
    public ResponseEntity<?> getProfileByPhone(@PathVariable("phone") Long phone){
        return userService.getProfileByPhone(phone);
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileDTO userProfileDTO,HttpServletRequest request){
        if(userProfileDTO.isNullEntriesPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("One or more fields are empty.");
        if(!jwtHelper.getUserIdAuthenticated(userProfileDTO.getUserID(),request))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please update your own profile...");

        return userService.updateProfile(userProfileDTO);
    }
}
