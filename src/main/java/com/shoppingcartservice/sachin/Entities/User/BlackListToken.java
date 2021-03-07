package com.shoppingcartservice.sachin.Entities.User;

import javax.persistence.*;
import java.util.Date;

@Entity
public class BlackListToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemID;
    @Column(unique = true)
    private String tokenID;
    private Date expiryDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserCredentials userCredentials;

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date date) {
        this.expiryDate = date;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }
}
