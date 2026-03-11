package com.jitendra.userservice.dto;

import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String status;

    // getters and setters
}
