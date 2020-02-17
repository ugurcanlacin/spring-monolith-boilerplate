package com.monolith.boilerplate;

import com.monolith.boilerplate.model.*;
import com.monolith.boilerplate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Profile("mysql")
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup)
            return;
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
        VerificationToken token = VerificationToken.builder().token("token").expiresAt(LocalDateTime.now().plusDays(1)).build();
        Set<VerificationToken> tokens = new HashSet<>();
        tokens.add(token);
        User user = User.builder().email("test@email.com")
                .emailVerified(true)
                .name("name")
                .surname("surname")
                .password(passwordEncoder.encode("123"))
                .imageUrl("url")
                .provider(AuthProvider.app)
                .roles(roles)
                .verificationTokens(tokens)
                .build();
        userRepository.save(user);
        alreadySetup = true;
    }
}