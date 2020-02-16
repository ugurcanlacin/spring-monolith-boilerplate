package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Privilege;
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
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TestEntityManager tem;
    @Autowired
    PrivilegeRepository privilegeRepository;

    @BeforeEach
    private void saveTestRole() {
        Set<Privilege> privileges = new HashSet<>();
        privileges.add(Privilege.builder().name("WRITE_ORGANIZATION_A").build());
        privileges.add(Privilege.builder().name("READ_ORGANIZATION_A").build());
        Role role = Role.builder().name("ROLE").privileges(privileges).build();
        roleRepository.save(role);
    }

    @Test
    public void shouldFindRoleByName() {
        Role roleByName = roleRepository.findByName("ROLE");
        Assertions.assertEquals(2, roleByName.getPrivileges().size());
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
        Set<Privilege> privileges = new HashSet<>();
        Privilege privilege = privilegeRepository.findByName("WRITE_ORGANIZATION_A");
        privileges.add(privilege);
        Role role = Role.builder().name("ROLE2").privileges(privileges).build();
        roleRepository.save(role);
        Role role2 = roleRepository.findByName("ROLE2");
        Set<Privilege> privilegesSets= role2.getPrivileges();
        assertNotNull(role2);
        assertNotNull(privilegesSets);
        assertEquals(1, privilegesSets.size());
        assertEquals("WRITE_ORGANIZATION_A", privilegesSets.iterator().next().getName());
    }

    @Test
    public void removingRoleShouldNotRemovePrivileges(){
        roleRepository.deleteByName("ROLE");
        roleRepository.flush();
        assertEquals(0, roleRepository.count());
        assertEquals(2, privilegeRepository.count());
    }
}
