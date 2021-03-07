package com.shoppingcartservice.sachin.Reposistories.User;

import com.shoppingcartservice.sachin.Entities.User.BlackListToken;
import com.shoppingcartservice.sachin.Entities.User.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlackListTokenRepo extends JpaRepository<BlackListToken,String> {
    BlackListToken findBlackListTokenByTokenID(String tokenID);
    List<BlackListToken> findBlackListTokensByUserCredentials(UserCredentials userCredentials);
}
