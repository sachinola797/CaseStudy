package com.shoppingcartservice.sachin.Services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.shoppingcartservice.sachin.Entities.User.BlackListToken;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Reposistories.User.BlackListTokenRepo;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlackListTokenService {

    @Autowired
    private BlackListTokenRepo blackListTokenRepo;
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;
    @Autowired
    JwtHelper jwtHelper;

    public void addTokenToBlackList(DecodedJWT token, UserCredentials userCredentials) {
        BlackListToken blackListToken = new BlackListToken();
        blackListToken.setTokenID(token.getId());
        blackListToken.setExpiryDate(token.getExpiresAt());
        blackListToken.setUserCredentials(userCredentials);
        blackListTokenRepo.save(blackListToken);
    }

    public void cleanupTokens(UserCredentials userCredentials) {
        List<BlackListToken> tokens = blackListTokenRepo.findBlackListTokensByUserCredentials(userCredentials);
        for (BlackListToken token : tokens) {
            if (token.getExpiryDate().before(new Date())) blackListTokenRepo.delete(token);
        }
    }

    public ResponseEntity<?> logoutFromAllDevices(Long userId){
        UserCredentials userCredentials=userCredentialsRepo.findByUserProfile_UserID(userId);
        userCredentials.setDenyBefore(new Date());
        userCredentialsRepo.save(userCredentials);
        return ResponseEntity.ok("Logged out from all devices successfully!!!");
    }
}
