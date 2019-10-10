package com.shoppingcartservice.sachin.Services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.shoppingcartservice.sachin.Config.JwtTokenUtil;
import com.shoppingcartservice.sachin.Entities.User.Address;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Config.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.AddressRepo;
import com.shoppingcartservice.sachin.Reposistories.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserCredentialsRepo userCredentialsRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AddressRepo addressRepo;

    public ResponseEntity<?> login(UserCredentials user) {
        UserCredentials currentUser=userCredentialsRepo.findByEmail(user.getEmail());
        if(currentUser==null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User doesn't exist!!!");
        else if(BCrypt.checkpw(user.getPassword(),currentUser.getPassword())){
            final String token = jwtTokenUtil.generateToken(currentUser);
            currentUser.setToken(token);
            userCredentialsRepo.save(currentUser);
            return ResponseEntity.ok("Success "+token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password!!!");
    }

    public ResponseEntity<?> signup(String name,String email,String password) {
        if(userCredentialsRepo.findByEmail(email)!=null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User already exist!!!");

        UserCredentials user=new UserCredentials();
        UserProfile userProfile=new UserProfile();
        userProfile.setName(name);
        userProfile.setEmail(email);
        userProfileRepo.save(userProfile);

        user.setEmail(email);
        user.setPassword(BCrypt.hashpw(password,BCrypt.gensalt(12)));
        user.setUserProfile(userProfile);
        final String token = jwtTokenUtil.generateToken(user);
        user.setToken(token);
        userCredentialsRepo.save(user);
        return ResponseEntity.ok("Success "+token);

    }

    public ResponseEntity<?> updateProfile(ObjectNode details){
        String email=details.get("email").asText();
        int userID=details.get("userID").asInt();


        UserProfile userProfile=userProfileRepo.getUserProfileByUserID(userID);
        UserCredentials userCredentials=userCredentialsRepo.findByUserProfile(userProfile);

        userProfile.setName(details.get("name").asText());
        userProfile.setEmail(email);
        userProfile.setPhone(details.get("phone").asInt());

        Address address;
        if(userProfile.getAddress()!=null)
            address=userProfile.getAddress();
        else
            address=new Address();


        address.setStreet(details.get("address").get("street").asText());
        address.setCity(details.get("address").get("city").asText());
        address.setState(details.get("address").get("state").asText());
        address.setPincode(details.get("address").get("pincode").asInt());
        addressRepo.save(address);
        userProfile.setAddress(address);
        userProfileRepo.save(userProfile);
        userCredentialsRepo.save(userCredentials);

        return ResponseEntity.ok("User Details updated successfully!!!");
    }

    public ResponseEntity<?> getProfile(Integer userId){
        UserProfile user=userProfileRepo.getUserProfileByUserID(userId);
        if(user==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User doesn't exist!!!");

        return ResponseEntity.ok(user);
    }

}
