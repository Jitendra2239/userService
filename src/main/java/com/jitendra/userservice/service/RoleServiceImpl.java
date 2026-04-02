package com.jitendra.userservice.service;

import com.jitendra.userservice.dto.RoleDto;
import com.jitendra.userservice.exception.DuplicateResourceException;
import com.jitendra.userservice.exception.ResourceNotFoundException;
import com.jitendra.userservice.model.Role;
import com.jitendra.userservice.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleDto createRole(RoleDto dto) {

        roleRepository.findByRoleNameIgnoreCase(dto.getRoleName())
                .ifPresent(r -> {
                    throw new DuplicateResourceException("Role already exists");
                });

        Role role = new Role();
        role.setRoleName(dto.getRoleName());

        Role saved = roleRepository.save(role);

        return RoleDto.builder()

                .roleName(saved.getRoleName())
                .build();
    }

    @Override
    public RoleDto getRoleById(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + id));

        return RoleDto.builder()

                .roleName(role.getRoleName())
                .build();
    }

    @Override
    public List<RoleDto> getAllRoles() {

        return roleRepository.findAll()
                .stream()
                .map(role -> RoleDto.builder()

                        .roleName(role.getRoleName())
                        .build())
                .toList();
    }
}