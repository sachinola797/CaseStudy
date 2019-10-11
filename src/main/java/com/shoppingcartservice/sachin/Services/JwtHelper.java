package com.shoppingcartservice.sachin.Services;


import com.auth0.jwt.JWT;
import com.shoppingcartservice.sachin.Config.JwtProperties;
import com.shoppingcartservice.sachin.Config.UserPrincipal;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Entities.User.UserProfile;
import com.shoppingcartservice.sachin.Reposistories.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.UserProfileRepo;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.PresentationDirection;
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

    public boolean getUserIdAuthenticated(Integer userId,HttpServletRequest request){

        String email=getEmailFromJwt(request);
        UserCredentials userCredentials=userCredentialsRepo.findByEmail(email);
        UserProfile user=userCredentials.getUserProfile();
        if(user!=null){
            if(user.getUserID()==userId)
                return true;
        }
        return false;
    }

    public Integer getCurrentUserId(HttpServletRequest request){
        String email=getEmailFromJwt(request);
        UserCredentials userCredentials=userCredentialsRepo.findByEmail(email);
        UserProfile user=userCredentials.getUserProfile();
        if(user!=null){
            return user.getUserID();
        }
        return null;
    }
}
