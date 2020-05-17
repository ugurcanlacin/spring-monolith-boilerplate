package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
    PermissionEntity findByName(String name);
    void deleteByName(String name);
}
