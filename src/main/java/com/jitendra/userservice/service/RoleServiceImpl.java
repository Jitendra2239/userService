package com.jitendra.userservice.service;

import com.jitendra.userservice.exception.ResourceNotFoundException;
import com.jitendra.userservice.model.Role;
import com.jitendra.userservice.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("No roles found");
        }

        return roles;
    }
}