package com.jitendra.userservice.service;

import com.jitendra.userservice.model.Role;
import com.jitendra.userservice.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
        }
    }
}