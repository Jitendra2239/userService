package com.jitendra.userservice.service;



import com.jitendra.userservice.dto.UserRequestDto;
import com.jitendra.userservice.dto.UserResponseDto;
import com.jitendra.userservice.model.Users;

import java.util.List;

public interface UserService {

    public UserResponseDto createUser(UserRequestDto dto);

    public UserResponseDto getUserById(Long id) ;

    public List<UserResponseDto> getAllUsers() ;

    public UserResponseDto updateUser(Long id, UserRequestDto dto) ;

    public UserResponseDto assignRole(Long userId, String roleName);
    public void deleteUser(Long id);
}