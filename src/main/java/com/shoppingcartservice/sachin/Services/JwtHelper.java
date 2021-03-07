package com.shoppingcartservice.sachin.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shoppingcartservice.sachin.Config.JwtProperties;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Service
public class JwtHelper {
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;

    public DecodedJWT getDecodedTokenFromJwt(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if(header!= null) {
            String token=header.replace(JwtProperties.TOKEN_PREFIX,"");
            // parse the token and validate it
            DecodedJWT decodedToken = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
                    .build()
                    .verify(token);
            return decodedToken;
        }
        return null;
    }

    public boolean getUserIdAuthenticated(Long userId,HttpServletRequest request){

        String email= getDecodedTokenFromJwt(request).getSubject();
        UserCredentials userCredentials=userCredentialsRepo.findByEmail(email);
        if(userCredentials.getRoles().equals("ADMIN"))
            return true;
        UserProfile user=userCredentials.getUserProfile();
        if(user!=null && user.getUserID()==userId){
            return true;
        }
        return false;
    }


    public Long getCurrentUserId(HttpServletRequest request){
        String email= getDecodedTokenFromJwt(request).getSubject();
        UserCredentials userCredentials=userCredentialsRepo.findByEmail(email);
        UserProfile user=userCredentials.getUserProfile();
        if(user!=null){
            return user.getUserID();
        }
        return null;
    }
}
