package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);
    void deleteByEmail(String email);
    Boolean existsByEmail(String email);

    @Modifying
    @Query(value = "delete from users_roles where user_id = :user_id and role_id = :role_id", nativeQuery = true)
    void deleteRelationshipInUsersRolesByUserIdAndRoleId(@Param("user_id") String user_id, @Param("role_id") String role_id);

    @Modifying
    @Query(value = "delete from users_roles where role_id = :role_id", nativeQuery = true)
    void deleteAllRelationshipInUsersRolesByRoleId(@Param("role_id") String role_id);
}
