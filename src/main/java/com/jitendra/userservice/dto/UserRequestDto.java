package com.jitendra.userservice.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class UserRequestDto {


        private String name;
        private String email;
        private String password;
        private String phone;
        private  String role;
        private List<String> roles;
        // ✅ List instead of single
        private List<AddressDto> addresses;
    }