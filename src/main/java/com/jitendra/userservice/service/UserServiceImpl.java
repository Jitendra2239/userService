package com.jitendra.userservice.service;

import com.jitendra.userservice.exception.ResourceNotFoundException;
import com.jitendra.userservice.exception.UserAlreadyExistsException;
import com.jitendra.userservice.model.Users;
import com.jitendra.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users createUser(Users user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<Users> getUserById(Long id) {
        Optional<Users> user = userRepository.findById(id);
        if (user == null) {
                     throw   new ResourceNotFoundException("User not found with id: " + id);
        }
        return  user;
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
       Optional<Users> user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        return user;
    }

    @Override
    public List<Users> getAllUsers() {

        List<Users> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }

        return users;
    }

    @Override
    public Users updateUser(Long id, Users user) {

        Users existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setName(user.getName());
        existingUser.setPhone(user.getPhone());
        existingUser.setStatus(user.getStatus());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }
}