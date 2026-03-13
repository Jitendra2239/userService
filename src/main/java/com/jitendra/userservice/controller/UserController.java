package com.jitendra.userservice.controller;




import com.jitendra.userservice.dto.UserRequestDto;
import com.jitendra.userservice.dto.UserResponseDto;
import com.jitendra.userservice.model.Users;
import com.jitendra.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE USER
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {

        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());

        Users savedUser = userService.createUser(user);

        return ResponseEntity.ok(convertToResponseDto(savedUser));
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        Optional<Users> user = userService.getUserById(id);

        return ResponseEntity.ok(convertToResponseDto(user.get()));
    }

    // GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        List<UserResponseDto> users =
                userService.getAllUsers()
                        .stream()
                        .map(this::convertToResponseDto)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto dto) {

        Users user = new Users();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());

        Users updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(convertToResponseDto(updatedUser));
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully");
    }

    // DTO MAPPER
    private UserResponseDto convertToResponseDto(Users user) {

        UserResponseDto dto = new UserResponseDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus().toString());

        return dto;
    }
}