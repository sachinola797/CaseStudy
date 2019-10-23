package com.shoppingcartservice.sachin.Config;

import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
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
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        UserCredentials user=userCredentialsRepo.findByEmail(jwtHelper.getEmailFromJwt(httpServletRequest));
        if(user.getToken()==null){
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                httpServletResponse.getWriter().write("User has been already logged out !!!");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;

        }

        user.setToken(null);
        userCredentialsRepo.save(user);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.setHeader("Content-Type","application/json");
        try {
            httpServletResponse.getWriter().write("{\"result\":\"success\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
