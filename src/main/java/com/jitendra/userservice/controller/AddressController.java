package com.jitendra.userservice.controller;

import com.jitendra.userservice.dto.AddressDto;
import com.jitendra.userservice.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @PostMapping("/users/{userId}")
    public ResponseEntity<AddressDto> addAddress(@PathVariable Long userId,
                                                 @Valid @RequestBody AddressDto dto) {
        AddressDto response = addressService.addAddress(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/users/{userId}")
    public ResponseEntity<List<AddressDto>> getUserAddresses(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @GetMapping("/internal/users/{userId}")
    public ResponseEntity<List<AddressDto>> getUserAddressesInternal(@PathVariable Long userId) {
        System.out.println("userId-----> = " + userId);
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @GetMapping("/users/{userId}/default")
    public ResponseEntity<AddressDto> getDefaultAddress(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getDefaultAddress(userId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}