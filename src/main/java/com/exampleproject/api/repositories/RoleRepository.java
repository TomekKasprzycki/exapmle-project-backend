package com.exampleproject.api.repositories;

import com.exampleproject.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> getRoleById(Long id);

    Optional<Role> getRoleByName(String roleName);
}
