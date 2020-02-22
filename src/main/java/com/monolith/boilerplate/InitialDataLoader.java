package com.monolith.boilerplate;

import com.monolith.boilerplate.model.*;
import com.monolith.boilerplate.repository.RoleRepository;
import com.monolith.boilerplate.repository.UserRepository;
import com.monolith.boilerplate.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Profile("mysql")
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;
        Permission p1 = Permission.builder().name("P1").build();
        Permission p2 = Permission.builder().name("P2").build();
        Set<Permission> privileges1 = new HashSet<>();
        privileges1.add(p1);
        Set<Permission> privileges2 = new HashSet<>();
        privileges2.add(p2);
        Role role1 = Role.builder().name("ROLE1").permissions(privileges1).build();
        Role role2 = Role.builder().name("ROLE2").permissions(privileges2).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        roleRepository.saveAll(roles);

        User user = User.builder().email("test@email.com")
                .emailVerified(true)
                .name("name")
                .surname("surname")
                .password(passwordEncoder.encode("123"))
                .imageUrl("url")
                .provider(AuthProvider.app)
                .verificationTokens(new HashSet<>())
                .build();
        List<Role> rolesList = roleRepository.findAll();
        user.setRoles(new HashSet<>(rolesList));
        User userSaved = userRepository.save(user);

        VerificationToken token = VerificationToken.builder().isVerified(false).token("token").user(user).expiresAt(LocalDateTime.now().plusDays(1)).build();
        verificationTokenRepository.save(token);

        alreadySetup = true;
    }
}