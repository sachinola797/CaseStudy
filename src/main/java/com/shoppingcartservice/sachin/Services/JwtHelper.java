package com.shoppingcartservice.sachin.Services;


import com.auth0.jwt.JWT;
import com.shoppingcartservice.sachin.Config.JwtProperties;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.User.UserProfileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Service
public class JwtHelper {
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;
    @Autowired
    private UserProfileRepo userProfileRepo;

    public String getEmailFromJwt(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if(header!= null) {
            String token=header.replace(JwtProperties.TOKEN_PREFIX,"");
            // parse the token and validate it
            String email = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
            return email;
        }
        return null;
    }

    public boolean getUserIdAuthenticated(Long userId,HttpServletRequest request){

        String email=getEmailFromJwt(request);
        UserCredentials userCredentials=userCredentialsRepo.findByEmail(email);
        if(userCredentials.getRoles().equals("ADMIN"))
            return true;
        UserProfile user=userCredentials.getUserProfile();
        if(user!=null){
            if(user.getUserID()==userId)
                return true;
        }
        return false;
    }


    public Long getCurrentUserId(HttpServletRequest request){
        String email=getEmailFromJwt(request);
        UserCredentials userCredentials=userCredentialsRepo.findByEmail(email);
        UserProfile user=userCredentials.getUserProfile();
        if(user!=null){
            return user.getUserID();
        }
        return null;
    }
}
