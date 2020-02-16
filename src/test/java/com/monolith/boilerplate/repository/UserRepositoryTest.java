package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.AuthProvider;
import com.monolith.boilerplate.model.Privilege;
import com.monolith.boilerplate.model.Role;
import com.monolith.boilerplate.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager tem;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    private void saveTestUser() {
        Privilege p1 = Privilege.builder().name("P1").build();
        Privilege p2 = Privilege.builder().name("P2").build();
        Set<Privilege> privileges1 = new HashSet<>();
        privileges1.add(p1);
        Set<Privilege> privileges2 = new HashSet<>();
        privileges2.add(p2);
        Role role1 = Role.builder().name("ROLE1").privileges(privileges1).build();
        Role role2 = Role.builder().name("ROLE2").privileges(privileges2).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        User user = User.builder().email("test@email.com")
                .emailVerified(true)
                .name("name")
                .surname("surname")
                .password(passwordEncoder.encode("123"))
                .imageUrl("url")
                .provider(AuthProvider.app)
                .roles(roles)
                .build();
        userRepository.save(user);
    }

    @Test
    public void shouldHaveCorrectSizeAsBeforeEachMethod(){
        assertEquals(1, userRepository.count());
    }

    @Test
    public void shouldFindUserByName(){
        User user = userRepository.findByEmail("test@email.com");
        assertEquals("name", user.getName());
        assertEquals("url", user.getImageUrl());
    }

    @Test
    public void shouldFindRolesForUser(){
        User user = userRepository.findByEmail("test@email.com");
        assertNotNull(user.getRoles());
        assertEquals(2, user.getRoles().size());
        List<Role> roles = new ArrayList<>(user.getRoles());
        assertEquals(1, roles.get(0).getPrivileges().size());
        assertEquals(1, roles.get(1).getPrivileges().size());
    }

}
