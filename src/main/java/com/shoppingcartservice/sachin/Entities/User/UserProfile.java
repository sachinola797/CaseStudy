package com.shoppingcartservice.sachin.Entities.User;


import com.shoppingcartservice.sachin.Entities.User.Address;

import javax.persistence.*;

@Entity
public class UserProfile {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;
    private String name;
    private String email;
    @Column(unique = true)
    private long phone;
    @OneToOne
    @JoinColumn(name = "addressID")
    private Address address;

    public UserProfile() {}

    public UserProfile(String name, String email, long phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
