package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.*;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserEntityRepositoryTest {

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
        PermissionEntity p1 = PermissionEntity.builder().name("P1").build();
        PermissionEntity p2 = PermissionEntity.builder().name("P2").build();
        Set<PermissionEntity> permissions1 = new HashSet<>();
        permissions1.add(p1);
        Set<PermissionEntity> permissions2 = new HashSet<>();
        permissions2.add(p2);
        RoleEntity roleEntity1 = RoleEntity.builder().name("ROLE1").permissionEntities(permissions1).build();
        RoleEntity roleEntity2 = RoleEntity.builder().name("ROLE2").permissionEntities(permissions2).build();
        Set<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);
        roleRepository.saveAll(roleEntities);
        List<RoleEntity> allRoleEntities = roleRepository.findAll();
        VerificationTokenEntity token = VerificationTokenEntity.builder().verified(false).token("token").expiresAt(LocalDateTime.now().plusDays(1)).build();
        Set<VerificationTokenEntity> tokens = new HashSet<>();
        tokens.add(token);
        UserEntity user = UserEntity.builder().email("test@email.com")
                .emailVerified(true)
                .name("name")
                .surname("surname")
                .password(passwordEncoder.encode("123"))
                .imageUrl("url")
                .provider(AuthProvider.app)
                .roleEntities(new HashSet<>(allRoleEntities))
                .verificationTokenEntities(tokens)
                .build();
        UserEntity savedUser = userRepository.save(user);
    }

    @Test
    public void shouldHaveCorrectSizeAsBeforeEachMethod(){
        assertEquals(1, userRepository.count());
    }

    @Test
    public void shouldFindUserByEmail(){
        UserEntity user = userRepository.findByEmail("test@email.com");
        assertEquals("name", user.getName());
        assertEquals("url", user.getImageUrl());
        assertNotNull(user.getVerificationTokenEntities());
        assertEquals(1, user.getVerificationTokenEntities().size());
    }

    @Test
    public void shouldFindRolesForUser(){
        UserEntity user = userRepository.findByEmail("test@email.com");
        assertNotNull(user.getRoleEntities());
        assertEquals(2, user.getRoleEntities().size());
        List<RoleEntity> roleEntities = new ArrayList<>(user.getRoleEntities());
        assertEquals(1, roleEntities.get(0).getPermissionEntities().size());
        assertEquals(1, roleEntities.get(1).getPermissionEntities().size());
    }

    @Test
    @Ignore
    public void removingUserShouldNotRemoveRoles(){
        userRepository.deleteByEmail("test@email.com");
        UserEntity byEmail = userRepository.findByEmail("test@email.com");
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
