package com.shoppingcartservice.sachin.Config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shoppingcartservice.sachin.Entities.User.BlackListToken;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Reposistories.User.BlackListTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserCredentialsRepo userCredentialsRepo;
    private BlackListTokenRepo blackListTokenRepo;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserCredentialsRepo userCredentialsRepo, BlackListTokenRepo blackListTokenRepo) {
        super(authenticationManager);
        this.userCredentialsRepo = userCredentialsRepo;
        this.blackListTokenRepo = blackListTokenRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Read the Authorization header, where the JWT token should be
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        // If header does not contain BEARER or is null delegate to Spring impl and exit
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // If header is present, try grab user principal from database and perform authorization
        Authentication authentication = getUsernamePasswordAuthentication(request);
        if(authentication==null){
            response.addHeader("tokenValidity","expired");
            chain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Continue filter execution
        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if (header != null){
            String token=header.replace(JwtProperties.TOKEN_PREFIX,"");
            // parse the token and validate it
            String email;
            JWTVerifier verifier;
            DecodedJWT decodedToken;
            try {
                verifier = JWT.require(HMAC512(JwtProperties.SECRET.getBytes())).build();
                decodedToken = verifier.verify(token);
                email = decodedToken.getSubject();
            } catch (Exception e){
                e.getStackTrace();
                return null;
            }
            if (blackListTokenRepo.findBlackListTokenByTokenID(decodedToken.getId())!=null) {
                return null;
            }
            // Search in the DB if we find the user by token subject (username)
            if (email != null) {
                UserCredentials user = userCredentialsRepo.findByEmail(email);
                // checking if token before logoutFromAllDevices is being used
                if(user!=null && user.getDenyBefore()!=null && decodedToken.getIssuedAt().before(user.getDenyBefore()))
                    return null;
                UserPrincipal principal = new UserPrincipal(user);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, principal.getAuthorities());
                return auth;
            }
            return null;
        }
        return null;
    }
}
