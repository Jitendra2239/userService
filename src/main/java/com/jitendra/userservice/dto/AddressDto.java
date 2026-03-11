package com.jitendra.userservice.dto;

import lombok.Data;

@Data
public class AddressDto {

    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;

    // getters and setters
}
