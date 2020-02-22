package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Permission;
import com.monolith.boilerplate.model.Role;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PermissionRepositoryTest {
    @Autowired
    TestEntityManager tem;
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    private void saveTestPrivilege() {
        Permission p1 = Permission.builder().name("WRITE_ORGANIZATION_A").build();
        Permission p2 = Permission.builder().name("READ_ORGANIZATION_A").build();
        HashSet<Permission> permissions = new HashSet<>();
        permissions.add(p1);
        permissions.add(p2);
        Role role = Role.builder().name("ROLE").permissions(permissions).build();
        roleRepository.save(role);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionIfNewEntityHasDuplicateName() {
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            permissionRepository.save(Permission.builder().name("WRITE_ORGANIZATION_A").build());
            tem.flush();
        });
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }

    @Test
    public void removingPrivilegeShouldNotRemoveRole(){
    }
}
