package com.shoppingcartservice.sachin.Services;

import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import com.shoppingcartservice.sachin.Config.UserPrincipal;
import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserCredentials user = this.userCredentialsRepo.findByEmail(s);

        //self
        if(user==null)
            throw new UsernameNotFoundException("User doesn't exist...");
        //
        UserPrincipal userPrincipal = new UserPrincipal(user);

        return userPrincipal;
    }
}