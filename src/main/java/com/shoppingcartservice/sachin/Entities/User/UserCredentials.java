package com.shoppingcartservice.sachin.Entities.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
public class UserCredentials {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(unique =true)
    private String email;
    private String password;
    private int active=1;
    private String roles = "";
    private Date denyBefore;

    @OneToOne
    @JoinColumn(name = "userProfileID")
    private UserProfile userProfile;

    public UserCredentials() { this.roles="USER";}

    public UserCredentials(String email, String password, String roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getDenyBefore() {
        return denyBefore;
    }

    public void setDenyBefore(Date date) {
        this.denyBefore = date;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
