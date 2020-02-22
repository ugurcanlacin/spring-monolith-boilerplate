package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Permission;
import com.monolith.boilerplate.model.Role;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
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
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TestEntityManager tem;
    @Autowired
    PermissionRepository permissionRepository;

    @BeforeEach
    private void saveTestRole() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.builder().name("WRITE_ORGANIZATION_A").build());
        permissions.add(Permission.builder().name("READ_ORGANIZATION_A").build());
        Role role = Role.builder().name("ROLE").permissions(permissions).build();
        roleRepository.save(role);
    }

    @Test
    public void shouldFindRoleByName() {
        Role roleByName = roleRepository.findByName("ROLE");
        Assertions.assertEquals(2, roleByName.getPermissions().size());
    }

    @Test
    public void shouldThrowConstraintViolationExceptionIfNewEntityHasDuplicateName() {
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            roleRepository.save(Role.builder().name("ROLE").build());
            tem.flush();
        });
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }

    @Test
    public void shouldBeAbleToSaveAnotherRoleWithSamePrivilege() {
        Set<Permission> permissions = new HashSet<>();
        Permission permission = permissionRepository.findByName("WRITE_ORGANIZATION_A");
        permissions.add(permission);
        Role role = Role.builder().name("ROLE2").permissions(permissions).build();
        roleRepository.save(role);
        Role role2 = roleRepository.findByName("ROLE2");
        Set<Permission> permissionsSets= role2.getPermissions();
        assertNotNull(role2);
        assertNotNull(permissionsSets);
        assertEquals(1, permissionsSets.size());
        assertEquals("WRITE_ORGANIZATION_A", permissionsSets.iterator().next().getName());
    }

    @Test
    public void removingRoleShouldNotRemovePrivileges(){
        roleRepository.deleteByName("ROLE");
        roleRepository.flush();
        assertEquals(0, roleRepository.count());
        assertEquals(2, permissionRepository.count());
    }
}
