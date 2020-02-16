package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role findByName(String name);
    void deleteByName(String name);
}
