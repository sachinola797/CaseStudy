package com.shoppingcartservice.sachin.Config;

import com.auth0.jwt.JWT;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Services.BlackListTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.*;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private AuthenticationSuccessHandler successHandler = new LoginSuccessHandlerImpl();
    private AuthenticationFailureHandler failureHandler = new LoginFailureHandlerImpl();
    private UserCredentialsRepo userCredentialsRepo;
    private BlackListTokenService blackListTokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserCredentialsRepo userCredentialsRepo, BlackListTokenService blackListTokenService) {
        this.authenticationManager = authenticationManager;
        this.userCredentialsRepo=userCredentialsRepo;
        this.blackListTokenService=blackListTokenService;
    }

    /* Trigger when we issue POST request to /login
    We also need to pass in {"username":"dan", "password":"dan123"} in the request body
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // Create login token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getParameter("username"),
                request.getParameter("password"),
                new ArrayList<>());

        // Authenticate user
        Authentication auth = authenticationManager.authenticate(authenticationToken);
        return auth;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // Grab principal
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        Date issueDate = new Date();
        Date expiryDate = new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME);

        // Create JWT Token
        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withJWTId(UUID.randomUUID().toString())
                .withIssuedAt(issueDate)
                .withExpiresAt(expiryDate)
                .sign(HMAC512(JwtProperties.SECRET.getBytes()));
        UserCredentials user=userCredentialsRepo.findByEmail(principal.getUsername());
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + token);
        response.addHeader("userId",user.getUserProfile().getUserID()+"");
        response.addHeader("role",user.getRoles());

        // cleanup expired black-listed tokens
        blackListTokenService.cleanupTokens(user);

        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString(), failed);
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
            logger.debug("Delegating to authentication failure handler " + failureHandler);
        }
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
