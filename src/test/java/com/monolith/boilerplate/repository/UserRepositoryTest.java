package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    TestEntityManager tem;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    private void saveTestUser() {
        Permission p1 = Permission.builder().name("P1").build();
        Permission p2 = Permission.builder().name("P2").build();
        Set<Permission> permissions1 = new HashSet<>();
        permissions1.add(p1);
        Set<Permission> permissions2 = new HashSet<>();
        permissions2.add(p2);
        Role role1 = Role.builder().name("ROLE1").permissions(permissions1).build();
        Role role2 = Role.builder().name("ROLE2").permissions(permissions2).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        roleRepository.saveAll(roles);
        List<Role> allRoles = roleRepository.findAll();
        VerificationToken token = VerificationToken.builder().isVerified(false).token("token").expiresAt(LocalDateTime.now().plusDays(1)).build();
        Set<VerificationToken> tokens = new HashSet<>();
        tokens.add(token);
        User user = User.builder().email("test@email.com")
                .emailVerified(true)
                .name("name")
                .surname("surname")
                .password(passwordEncoder.encode("123"))
                .imageUrl("url")
                .provider(AuthProvider.app)
                .roles(new HashSet<>(allRoles))
                .verificationTokens(tokens)
                .build();
        User savedUser = userRepository.save(user);
    }

    @Test
    public void shouldHaveCorrectSizeAsBeforeEachMethod(){
        assertEquals(1, userRepository.count());
    }

    @Test
    public void shouldFindUserByEmail(){
        User user = userRepository.findByEmail("test@email.com");
        assertEquals("name", user.getName());
        assertEquals("url", user.getImageUrl());
        assertNotNull(user.getVerificationTokens());
        assertEquals(1, user.getVerificationTokens().size());
    }

    @Test
    public void shouldFindRolesForUser(){
        User user = userRepository.findByEmail("test@email.com");
        assertNotNull(user.getRoles());
        assertEquals(2, user.getRoles().size());
        List<Role> roles = new ArrayList<>(user.getRoles());
        assertEquals(1, roles.get(0).getPermissions().size());
        assertEquals(1, roles.get(1).getPermissions().size());
    }

    @Test
    public void removingUserShouldNotRemoveRoles(){
        userRepository.deleteByEmail("test@email.com");
        User byEmail = userRepository.findByEmail("test@email.com");
        assertNull(byEmail);
        assertEquals(2, roleRepository.count());
    }

    @Test
    public void createdDateShouldNotBeNull(){
        // TODO
//        User user = userRepository.findByEmail("test@email.com");
//        assertNotNull(user.getCreatedAt());
    }

    @Test
    public void removingRoleShouldNotRemoveUser(){
        // TODO
    }

    @Test
    public void removedRoleShouldBeRemovedFromUserToo(){
        // TODO
    }

    @Test
    public void removedVerificationTokenShouldBeRemovedFromUserToo(){
        // TODO
    }
}
