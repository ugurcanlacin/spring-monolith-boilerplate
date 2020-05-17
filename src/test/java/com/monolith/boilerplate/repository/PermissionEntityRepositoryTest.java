package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.PermissionEntity;
import com.monolith.boilerplate.model.RoleEntity;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PermissionEntityRepositoryTest {
    @Autowired
    TestEntityManager tem;
    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    private void saveTestPrivilege() {
        PermissionEntity p1 = PermissionEntity.builder().name("WRITE_ORGANIZATION_A").build();
        PermissionEntity p2 = PermissionEntity.builder().name("READ_ORGANIZATION_A").build();
        HashSet<PermissionEntity> permissions = new HashSet<>();
        permissions.add(p1);
        permissions.add(p2);
        RoleEntity roleEntity = RoleEntity.builder().name("ROLE").permissionEntities(permissions).build();
        roleRepository.save(roleEntity);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionIfNewEntityHasDuplicateName() {
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            permissionRepository.save(PermissionEntity.builder().name("WRITE_ORGANIZATION_A").build());
            tem.flush();
        });
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }

    @Test
    public void removingPrivilegeShouldNotRemoveRole(){
    }
}
