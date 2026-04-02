package com.jitendra.userservice.dto;

import com.jitendra.userservice.dto.*;
import com.jitendra.userservice.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class UserMapper {


    public static Users toEntity(UserRequestDto dto) {

        if (dto == null) return null;

        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // encode in service
        user.setPhone(dto.getPhone());

        return user;
    }


    public static UserResponseDto toDto(Users user) {

        if (user == null) return null;

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus() != null ? user.getStatus().name() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())


                .roles(mapRoles(user.getRoles()))
                .addresses(mapAddresses(user.getAddresses()))

                .build();
    }


    private static Set<RoleDto> mapRoles(Set<Role> roles) {

        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }

        return roles.stream()
                .filter(Objects::nonNull)
                .map(role -> RoleDto.builder()

                        .roleName(role.getRoleName())
                        .build())
                .collect(Collectors.toSet());
    }


    private static Set<AddressDto> mapAddresses(Set<Address> addresses) {

        if (addresses == null || addresses.isEmpty()) {
            return Collections.emptySet();
        }

        return addresses.stream()
                .filter(Objects::nonNull)
                .map(addr -> AddressDto.builder()

                        .street(addr.getStreet())
                        .city(addr.getCity())
                        .state(addr.getState())
                        .pincode(addr.getPincode())
                        .country(addr.getCountry())
                        .isDefault(Boolean.TRUE.equals(addr.getIsDefault()))
                        .build())
                .collect(Collectors.toSet());
    }


    public static void updateEntity(Users user, UserRequestDto dto) {

        if (dto == null || user == null) return;

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(dto.getPassword());
        }
    }
}