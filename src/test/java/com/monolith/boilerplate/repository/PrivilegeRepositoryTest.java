package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Privilege;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class PrivilegeRepositoryTest {
    @Autowired
    TestEntityManager tem;
    @Autowired
    PrivilegeRepository privilegeRepository;

    @BeforeEach
    private void saveTestPrivilege() {
        Privilege p1 = Privilege.builder().name("WRITE_ORGANIZATION_A").build();
        Privilege p2 = Privilege.builder().name("READ_ORGANIZATION_A").build();
        privilegeRepository.save(p1);
        privilegeRepository.save(p2);
    }

    @Test
    public void shouldThrowConstraintViolationExceptionIfNewEntityHasDuplicateName() {
        PersistenceException exception = assertThrows(PersistenceException.class, () -> {
            privilegeRepository.save(Privilege.builder().name("WRITE_ORGANIZATION_A").build());
            tem.flush();
        });
        assertTrue(exception.getCause() instanceof ConstraintViolationException);
    }
}
