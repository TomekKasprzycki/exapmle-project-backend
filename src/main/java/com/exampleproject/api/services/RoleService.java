package com.exampleproject.api.services;

import com.exampleproject.api.model.Role;
import com.exampleproject.api.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> getRole(Long id) {
        return roleRepository.getRoleById(id);
    }

    public Optional<Role> findByName(String roleName) {
        return roleRepository.getRoleByName(roleName);
    }

    public Optional<Role> getRoleByName(String role) {
        return roleRepository.getRoleByName(role);
    }
}
