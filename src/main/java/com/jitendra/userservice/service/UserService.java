package com.jitendra.userservice.service;

import com.jitendra.userservice.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Users createUser(Users user);

    Optional<Users> getUserById(Long id);

    Optional<Users> getUserByEmail(String email);

    List<Users> getAllUsers();

    Users updateUser(Long id, Users user);

    void deleteUser(Long id);
}