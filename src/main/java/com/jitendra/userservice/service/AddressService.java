package com.jitendra.userservice.service;

import com.jitendra.userservice.model.Address;

import java.util.List;

public interface AddressService {

    Address addAddress(Address address);

    List<Address> getUserAddresses(Long userId);

    void deleteAddress(Long id);
}