package com.jitendra.userservice.service;

import com.jitendra.userservice.model.Role;

import java.util.List;

public interface RoleService {

    Role createRole(Role role);

    List<Role> getAllRoles();
}