package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    RoleEntity findByName(String name);
    void deleteByName(String name);

    @Modifying
    @Query(value = "delete from roles_permissions where role_id = :role_id and permission_id = :permission_id", nativeQuery = true)
    void deleteRelationshipInRolesPermissionsByUserIdAndRoleId(@Param("role_id") String role_id, @Param("permission_id") String permission_id);

    @Modifying
    @Query(value = "delete from roles_permissions where role_id = :role_id", nativeQuery = true)
    void deleteAllRelationshipInRolesPermissionsByRoleId(@Param("role_id") String role_id);

    @Modifying
    @Query(value = "delete from roles_permissions where permission_id = :permission_id", nativeQuery = true)
    void deleteAllRelationshipInRolesPermissionsByPermissionId(@Param("permission_id") String permission_id);
}
