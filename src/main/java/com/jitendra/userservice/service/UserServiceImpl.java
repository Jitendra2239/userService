package com.jitendra.userservice.service;


import com.jitendra.event.UserCreatedEvent;
import com.jitendra.event.UserUpdatedEvent;
import com.jitendra.userservice.dto.*;
import com.jitendra.userservice.exception.DuplicateResourceException;
import com.jitendra.userservice.exception.ResourceNotFoundException;
import com.jitendra.userservice.model.Address;
import com.jitendra.userservice.model.Role;
import com.jitendra.userservice.model.UserStatus;
import com.jitendra.userservice.model.Users;
import com.jitendra.userservice.repository.RoleRepository;
import com.jitendra.userservice.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
   private  final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserResponseDto createUser(UserRequestDto dto) {


        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        }


        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword())); // 🔐 encode later
        user.setPhone(dto.getPhone());
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Set<Role> roleSet = new HashSet<>();

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {

            for (String roleName : dto.getRoles()) {
                Role role = roleRepository.findByRoleNameIgnoreCase(roleName)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Role not found: " + roleName));
                roleSet.add(role);
            }

        } else {

            Role defaultRole = roleRepository.findByRoleNameIgnoreCase("USER")
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Default role USER not found"));
            roleSet.add(defaultRole);
        }

        user.setRoles(roleSet);


        Set<Address> addressList = new HashSet<>();

        if (dto.getAddresses() != null && !dto.getAddresses().isEmpty()) {

            for (int i = 0; i < dto.getAddresses().size(); i++) {

                AddressDto addrDto = dto.getAddresses().get(i);

                Address address = new Address();
                address.setStreet(addrDto.getStreet());
                address.setCity(addrDto.getCity());
                address.setState(addrDto.getState());
                address.setPincode(addrDto.getPincode());
                address.setCountry(addrDto.getCountry());


                address.setIsDefault(i == 0);


                address.setUser(user);

                addressList.add(address);
            }
        }

        user.setAddresses(addressList);


        Users savedUser = userRepository.save(user);


        UserCreatedEvent event = buildUserCreatedEvent(savedUser);
        kafkaTemplate.send("user-created", savedUser.getId().toString(), event);


        return UserMapper.toDto(savedUser);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {

        Users user = userRepository.findUserWithDetails(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + id));

        return UserMapper.toDto(user);
    }
    @Override
    @Transactional(readOnly = true)
    public UserContactDto findContact(Long id) {

     return  userRepository.findContact(id);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAllWithDetails()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }


    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + id));

        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        UserUpdatedEvent event = buildUserUpdatedEvent(user);
        kafkaTemplate.send("user-updated", user.getId().toString(), event);
        return UserMapper.toDto(userRepository.save(user));
    }


    @Override
    public void deleteUser(Long id) {

        Users user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found: " + id));

        user.setStatus(UserStatus.INACTIVE); // 🔥 soft delete
        userRepository.save(user);
    }

    @Override
    public UserDto findByEmail(String email) {
       return userRepository.findByEmail(email).orElseThrow(()->new org.apache.kafka.common.errors.ResourceNotFoundException("User Not found with email"+ email));

    }


    @Override
    public UserResponseDto assignRole(Long userId, String roleName) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findByRoleNameIgnoreCase(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.getRoles().add(role);

        return UserMapper.toDto(userRepository.save(user));
    }



    private UserCreatedEvent buildUserCreatedEvent(Users user) {

        UserCreatedEvent event = new UserCreatedEvent();

        event.setUserId(user.getId());
        event.setName(user.getName());
        event.setEmail(user.getEmail());
        event.setPhone(user.getPhone());
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
           if (user.getAddresses()==null)return null;
        return user.getAddresses()
                .stream()
                .filter(Address::getIsDefault)
                .findFirst().orElse(null);

    }
}