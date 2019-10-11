package com.shoppingcartservice.sachin.DTOs;

import com.shoppingcartservice.sachin.Entities.User.Address;

public class UserProfileDTO {
    private int userID;
    private String name;
    private String email;
    private int phone;
    private Address address;

    public boolean isNullEntriesPresent(){
        if(getUserID()==0 ||this.email==null|| getEmail().isEmpty()||this.name==null || getName().isEmpty() || getPhone()==0 || getAddress()==null)
            return true;
        if(getAddress()!=null){
            if(getAddress().getCity()==null || getAddress().getState()==null || getAddress().getStreet()==null ||getAddress().getCity().isEmpty()||getAddress().getState().isEmpty()||getAddress().getStreet().isEmpty() ||getAddress().getPincode()==0)
                return true;
        }
        return false;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}
