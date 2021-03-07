package com.shoppingcartservice.sachin.Entities.User;

import javax.persistence.*;

@Entity
public class Address {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private long addressID;
    private String street;
    private String city;
    private String state;
    @Column(nullable = true)
    private int pincode;

    public long getAddressID() {
        return addressID;
    }

    public void setAddressID(long addressID) {
        this.addressID = addressID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }
}
