package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    void deleteByEmail(String email);
    Boolean existsByEmail(String email);
}
