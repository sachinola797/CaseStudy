package com.shoppingcartservice.sachin.Config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Services.BlackListTokenService;
import com.shoppingcartservice.sachin.Services.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutHandlerImpl extends
        SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    UserCredentialsRepo userCredentialsRepo;
    @Autowired
    private BlackListTokenService blackListTokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        DecodedJWT decodedToken = jwtHelper.getDecodedTokenFromJwt(httpServletRequest);
        UserCredentials userCredentials=userCredentialsRepo.findByEmail(decodedToken.getSubject());

        blackListTokenService.addTokenToBlackList(decodedToken, userCredentials);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setHeader("Content-Type","application/json");
        try {
            httpServletResponse.getWriter().write("{\"result\":\"success\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
