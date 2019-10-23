package com.shoppingcartservice.sachin.Config;

import com.shoppingcartservice.sachin.Reposistories.User.UserCredentialsRepo;
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
    private LogoutHandlerImpl logoutHandler;

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
                .antMatchers("/order/{\\d+}/getOrders").hasAnyRole("USER","ADMIN")
                .antMatchers("/updateProfile","/getProfile/**","/cart/**","/order/**").hasRole("USER")
                .antMatchers("/products/addProduct","/products/updateProduct","/getProfileByPhone/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutHandler)
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