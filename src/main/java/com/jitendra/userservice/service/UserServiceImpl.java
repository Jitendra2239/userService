package com.jitendra.userservice.service;

import com.jitendra.event.UserCreatedEvent;
import com.jitendra.event.UserUpdatedEvent;
import com.jitendra.userservice.exception.ResourceNotFoundException;
import com.jitendra.userservice.exception.UserAlreadyExistsException;
import com.jitendra.userservice.model.Address;
import com.jitendra.userservice.model.Users;
import com.jitendra.userservice.model.Role;
import com.jitendra.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserRepository userRepository;



    @Override
    public Users createUser(Users user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }

        Users users= userRepository.save(user);
        UserCreatedEvent event = buildUserCreatedEvent(user);

        kafkaTemplate.send("user-created", user.getId().toString(), event);
        return users;
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

        Users users= userRepository.save(existingUser);
        UserUpdatedEvent event = buildUserUpdatedEvent(user);

        kafkaTemplate.send("user-updated", user.getId().toString(), event);
        return users;
    }

    @Override
    public void deleteUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    private UserCreatedEvent buildUserCreatedEvent(Users user) {

        UserCreatedEvent event = new UserCreatedEvent();

        event.setUserId(user.getId());
        event.setName(user.getName());
        event.setEmail(user.getEmail());

        // Default Address
        Address defaultAddress = getDefaultAddress(user);

        if (defaultAddress != null) {
            event.setAddressLine(defaultAddress.getStreet());
            event.setCity(defaultAddress.getCity());
            event.setState(defaultAddress.getState());
            event.setPincode(defaultAddress.getPincode());
        }

        // Roles
        event.setRoles(
                user.getRoles()
                        .stream()
                        .map(Role::getRoleName)
                        .toList()
        );

        // Metadata
        event.setEventId(UUID.randomUUID().toString());
        event.setVersion(1L);
        event.setTimestamp(System.currentTimeMillis());

        return event;
    }

    private UserUpdatedEvent buildUserUpdatedEvent(Users user) {

        UserUpdatedEvent event = new UserUpdatedEvent();

        event.setUserId(user.getId());
        event.setName(user.getName());
        event.setEmail(user.getEmail());

        Address defaultAddress = getDefaultAddress(user);

        if (defaultAddress != null) {
            event.setAddressLine(defaultAddress.getStreet());
            event.setCity(defaultAddress.getCity());
            event.setState(defaultAddress.getState());
            event.setPincode(defaultAddress.getPincode());
        }

        event.setRoles(
                user.getRoles()
                        .stream()
                        .map(Role::getRoleName)
                        .toList()
        );

        event.setEventId(UUID.randomUUID().toString());
        event.setVersion(1l); // IMPORTANT
        event.setTimestamp(System.currentTimeMillis());

        return event;
    }

    private Address getDefaultAddress(Users user) {
        return user.getAddresses()
                .stream()
                .filter(Address::getIsDefault)
                .findFirst()
                .orElse(null);
    }
}