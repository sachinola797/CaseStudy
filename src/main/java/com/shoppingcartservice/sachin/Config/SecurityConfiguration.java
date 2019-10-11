package com.shoppingcartservice.sachin.Config;

import com.shoppingcartservice.sachin.Reposistories.UserCredentialsRepo;
import com.shoppingcartservice.sachin.Services.UserPrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserPrincipalDetailsService userPrincipalDetailsService;
    @Autowired
    private UserCredentialsRepo userCredentialsRepo;

    @Autowired
    LoginSuccessHandlerImpl loginSuccessHandler;
    @Autowired
    LoginFailureHandlerImpl loginFailureHandler;
    @Autowired
    LogoutHandlerImpl logoutHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // remove csrf and state in session because in jwt we do not need them
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // add jwt filters (1. authentication, 2. authorization)
                .addFilter(new JwtAuthenticationFilter(authenticationManager(),this.userCredentialsRepo))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),  this.userCredentialsRepo))
                .authorizeRequests()
                // configure access rules
                .antMatchers("/updateProfile","/getProfile/*").hasRole("USER")
                .antMatchers("/products/addProduct","/products/updateProduct").hasRole("ADMIN")
                .antMatchers("/products/*").permitAll()
                .antMatchers("/cart/**").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .permitAll();


//                .authorizeRequests()
//                .antMatchers("/cart/**").hasRole("USER")
//                //.antMatchers("/products/**").hasAnyRole("ADMIN", "MANAGER")
//                .and()
//                .csrf().disable()
//                .formLogin()
//                .loginPage("/")
//                .loginProcessingUrl("/login").permitAll()
//                .usernameParameter("email")
//                .and()
//                .logout().logoutRequestMatcher( new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/");
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}