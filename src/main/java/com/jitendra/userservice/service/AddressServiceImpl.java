package com.jitendra.userservice.service;

import com.jitendra.userservice.exception.ResourceNotFoundException;
import com.jitendra.userservice.model.Address;
import com.jitendra.userservice.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {

        List<Address> addresses = addressRepository.findByUserId(userId);

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("No addresses found for user id: " + userId);
        }

        return addresses;
    }

    @Override
    public void deleteAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found with id: " + id));

        addressRepository.delete(address);
    }
}