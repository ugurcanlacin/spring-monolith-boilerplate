package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission findByName(String name);
    void deleteByName(String name);
}
