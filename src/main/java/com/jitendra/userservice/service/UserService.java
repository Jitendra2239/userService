package com.jitendra.userservice.service;



import com.jitendra.userservice.dto.UserContactDto;
import com.jitendra.userservice.dto.UserDto;
import com.jitendra.userservice.dto.UserRequestDto;
import com.jitendra.userservice.dto.UserResponseDto;
import com.jitendra.userservice.model.Users;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface UserService {

    public UserResponseDto createUser(UserRequestDto dto);

    public UserResponseDto getUserById(Long id) ;

    public List<UserResponseDto> getAllUsers() ;

    public UserResponseDto updateUser(Long id, UserRequestDto dto) ;

    public UserResponseDto assignRole(Long userId, String roleName);
    public void deleteUser(Long id);
    public UserDto findByEmail(String email);
    public UserContactDto findContact(Long id);
}