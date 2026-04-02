package com.jitendra.userservice.service;

import com.jitendra.userservice.model.Role;
import com.jitendra.userservice.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        try {
            if (roleRepository.count() == 0) {
                Role role = new Role();
                role.setRoleName("USER");
                roleRepository.save(role);
                Role role1 = new Role();
                role1.setRoleName("ADMIN");
                roleRepository.save(role1);
            }
        } catch (Exception e) {
            System.out.println("DB not ready yet");
        }
    }
}