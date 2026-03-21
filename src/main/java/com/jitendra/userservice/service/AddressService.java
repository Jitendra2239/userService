package com.jitendra.userservice.service;

import com.jitendra.userservice.dto.AddressDto;
import com.jitendra.userservice.model.Address;

import java.util.List;

public interface AddressService {

    public AddressDto addAddress(Long userId, AddressDto dto);

    List<AddressDto> getUserAddresses(Long userId);
    public AddressDto getDefaultAddress(Long userId);
    void deleteAddress(Long id);
}