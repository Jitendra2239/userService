package com.jitendra.userservice.service;

import com.jitendra.userservice.dto.AddressDto;
import com.jitendra.userservice.exception.ResourceNotFoundException;
import com.jitendra.userservice.model.Address;
import com.jitendra.userservice.model.Users;
import com.jitendra.userservice.repository.AddressRepository;
import com.jitendra.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressDto addAddress(Long userId, AddressDto dto) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // ✅ If default = true → unset previous default
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            addressRepository.findFirstByUserIdAndIsDefaultTrue(userId)
                    .ifPresent(existing -> {
                        existing.setIsDefault(false);
                        addressRepository.save(existing);
                    });
        }

        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPincode(dto.getPincode());
        address.setCountry(dto.getCountry());
        address.setIsDefault(dto.getIsDefault());
        address.setUser(user);

        Address saved = addressRepository.save(address);

        return AddressDto.builder()
                .id(saved.getId())
                .street(saved.getStreet())
                .city(saved.getCity())
                .state(saved.getState())
                .pincode(saved.getPincode())
                .country(saved.getCountry())
                .isDefault(saved.getIsDefault())
                .build();
    }

    @Override
    public List<AddressDto> getUserAddresses(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }

        return addressRepository.findByUserId(userId)
                .stream()
                .map(addr -> AddressDto.builder()
                        .id(addr.getId())
                        .street(addr.getStreet())
                        .city(addr.getCity())
                        .state(addr.getState())
                        .pincode(addr.getPincode())
                        .country(addr.getCountry())
                        .isDefault(addr.getIsDefault())
                        .build())
                .toList();
    }

    @Override
    public AddressDto getDefaultAddress(Long userId) {

        Address address = addressRepository.findFirstByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Default address not found"));

        return AddressDto.builder()
                .id(address.getId())
                .city(address.getCity())
                .isDefault(address.getIsDefault())
                .build();
    }

    @Override
    public void deleteAddress(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        addressRepository.delete(address);
    }
}