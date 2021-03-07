package com.shoppingcartservice.sachin.Reposistories.User;


import com.shoppingcartservice.sachin.Entities.User.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address,Long> {
    Address findAddressByAddressID(Long addressId);
}
