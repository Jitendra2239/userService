package com.jitendra.userservice.dto;


import lombok.Data;

@Data
public class UserRequestDto {

    private String name;
    private String email;
    private String password;
    private String phone;

    // getters and setters
}