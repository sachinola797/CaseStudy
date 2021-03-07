package com.shoppingcartservice.sachin.Services;

import com.shoppingcartservice.sachin.DTOs.UserProfileDTO;
import com.shoppingcartservice.sachin.Entities.User.Address;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.User.AddressRepo;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.User.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserCredentialsRepo userCredentialsRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> signup(String name,String email,String password,Long phone) {
        if(userCredentialsRepo.findByEmail(email)!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already with given email id!!!");
        if(userProfileRepo.getUserProfileByPhone(phone)!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already with given phone number!!!");

        UserCredentials user=new UserCredentials();
        UserProfile userProfile=new UserProfile();
        userProfile.setName(capitalise(name));
        userProfile.setEmail(email);
        userProfile.setPhone(phone);
        userProfileRepo.save(userProfile);

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserProfile(userProfile);
        userCredentialsRepo.save(user);
        return ResponseEntity.ok("{\"result\": \"success\"}");
    }

    public ResponseEntity<?> updateProfile(UserProfileDTO userProfileDTO){
        long userId=userProfileDTO.getUserID();
        String email=userProfileDTO.getEmail();
        UserCredentials userCredentials=userCredentialsRepo.findByUserProfile_UserID(userId);
        UserCredentials verifyEmail=userCredentialsRepo.findByEmail(email);
        if(verifyEmail!=null){
            if(verifyEmail.getUserId()!=userCredentials.getUserId())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Id is already taken!!!");
        }
        UserProfile verifyPhone=userProfileRepo.getUserProfileByPhone(userProfileDTO.getPhone());
        if(verifyPhone!=null){
            if(verifyPhone.getUserID()!=userId)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone no. is already taken!!!");
        }
        userCredentials.setEmail(email);
        userCredentialsRepo.save(userCredentials);

        UserProfile userProfile=userProfileRepo.getUserProfileByUserID(userId);
        userProfile.setEmail(email);
        userProfile.setName(userProfileDTO.getName());
        userProfile.setPhone(userProfileDTO.getPhone());
        Address address=userProfileDTO.getAddress();
        addressRepo.save(address);
        userProfile.setAddress(address);
        userProfileRepo.save(userProfile);

        return ResponseEntity.ok("User Details updated successfully!!!");
    }

    public ResponseEntity<?> getProfile(Long userId){
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity<?> getProfileByPhone(long phone){
        UserProfile user=userProfileRepo.getUserProfileByPhone(phone);
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User by this phone number doesn't exists !!!");
        return ResponseEntity.ok(user);
    }

    private   String capitalise(String s){
        String[] list=s.split(" ");
        StringBuilder finalString= new StringBuilder();
        for(String a:list) {
            if(a.length()>1)
                finalString.append(" "+a.substring(0, 1).toUpperCase()+a.substring(1).toLowerCase());
            else if(a.length()>0)
                finalString.append(" "+a.substring(0, 1).toUpperCase());
        }
        return  finalString.substring(1);
    }

}
