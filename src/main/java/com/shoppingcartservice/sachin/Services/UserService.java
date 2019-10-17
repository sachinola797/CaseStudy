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
//    public ResponseEntity<?> login(UserCredentials user) {
//        UserCredentials currentUser=userCredentialsRepo.findByEmail(user.getEmail());
//        if(currentUser==null)
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User doesn't exist!!!");
//        else if(BCrypt.checkpw(user.getPassword(),currentUser.getPassword())){
//            final String token = jwtTokenUtil.generateToken(currentUser);
//            currentUser.setToken(token);
//            userCredentialsRepo.save(currentUser);
//            return ResponseEntity.ok("Success "+token);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password!!!");
//    }

    public ResponseEntity<?> signup(String name,String email,String password) {
        if(userCredentialsRepo.findByEmail(email)!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already exist!!!");

        UserCredentials user=new UserCredentials();
        UserProfile userProfile=new UserProfile();
        userProfile.setName(name);
        userProfile.setEmail(email);
        userProfileRepo.save(userProfile);

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserProfile(userProfile);
        userCredentialsRepo.save(user);
        return ResponseEntity.ok("{\"result\": \"success\"}");

    }

    public ResponseEntity<?> updateProfile(UserProfileDTO userProfileDTO){
        int userId=userProfileDTO.getUserID();
        String email=userProfileDTO.getEmail();
        UserCredentials userCredentials=userCredentialsRepo.findByUserId(userId);
        UserCredentials verifyEmail=userCredentialsRepo.findByEmail(email);
        if(verifyEmail!=null){
            if(verifyEmail.getUserId()!=userId)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Id is already taken");
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

    public ResponseEntity<?> getProfile(Integer userId){
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        return ResponseEntity.ok(user);
    }

}
