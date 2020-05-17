package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.PermissionEntity;
import com.monolith.boilerplate.model.RoleEntity;
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
public class RoleEntityRepositoryTest {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TestEntityManager tem;
    @Autowired
    PermissionRepository permissionRepository;

    @BeforeEach
    private void saveTestRole() {
        Set<PermissionEntity> permissions = new HashSet<>();
        permissions.add(PermissionEntity.builder().name("WRITE_ORGANIZATION_A").build());
        permissions.add(PermissionEntity.builder().name("READ_ORGANIZATION_A").build());
        RoleEntity roleEntity = RoleEntity.builder().name("ROLE").permissionEntities(permissions).build();
        roleRepository.save(roleEntity);
    }

    @Test
    public void shouldFindRoleByName() {
        RoleEntity roleEntityByName = roleRepository.findByName("ROLE");
        Assertions.assertEquals(2, roleEntityByName.getPermissionEntities().size());
    }

    @Test
    public void shouldThrowConstraintViolationExceptionIfNewEntityHasDuplicateName() {
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            roleRepository.save(RoleEntity.builder().name("ROLE").build());
            tem.flush();
        });
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }

    @Test
    public void shouldBeAbleToSaveAnotherRoleWithSamePrivilege() {
        Set<PermissionEntity> permissions = new HashSet<>();
        PermissionEntity permission = permissionRepository.findByName("WRITE_ORGANIZATION_A");
        permissions.add(permission);
        RoleEntity roleEntity = RoleEntity.builder().name("ROLE2").permissionEntities(permissions).build();
        roleRepository.save(roleEntity);
        RoleEntity roleEntity2 = roleRepository.findByName("ROLE2");
        Set<PermissionEntity> permissionsSets= roleEntity2.getPermissionEntities();
        assertNotNull(roleEntity2);
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
