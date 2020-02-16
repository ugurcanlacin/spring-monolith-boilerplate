package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, String> {
    Privilege findByName(String name);
    void deleteByName(String name);
}
