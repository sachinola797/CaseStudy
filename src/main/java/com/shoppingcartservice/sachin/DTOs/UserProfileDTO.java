package com.shoppingcartservice.sachin.DTOs;

import com.shoppingcartservice.sachin.Entities.User.Address;

public class UserProfileDTO {
    private long userID;
    private String email;
    private String name;
    private long phone;
    private Address address;

    public boolean isNullEntriesPresent(){
        if(getUserID()==0 ||this.name==null ||this.email==null || getName().isEmpty() || getEmail().isEmpty()|| getPhone()==0 || getAddress()==null)
            return true;
        if(getAddress()!=null){
            if(getAddress().getCity()==null || getAddress().getState()==null || getAddress().getStreet()==null ||getAddress().getCity().isEmpty()||getAddress().getState().isEmpty()||getAddress().getStreet().isEmpty() ||getAddress().getPincode()==0)
                return true;
        }
        return false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
