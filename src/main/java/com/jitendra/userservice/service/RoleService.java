package com.jitendra.userservice.service;

import com.jitendra.userservice.dto.RoleDto;
import com.jitendra.userservice.model.Role;

import java.util.List;

public interface RoleService {

    public RoleDto createRole(RoleDto dto);

    public RoleDto getRoleById(Long id);
    List<RoleDto> getAllRoles();
}