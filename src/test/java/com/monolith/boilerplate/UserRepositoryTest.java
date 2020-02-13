package com.monolith.boilerplate;

import com.monolith.boilerplate.model.AuthProvider;
import com.monolith.boilerplate.model.User;
import com.monolith.boilerplate.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    private void saveTestUser() {
        User user = User.builder().email("test@email.com")
                .emailVerified(true)
                .name("name")
                .password(passwordEncoder.encode("123"))
                .surname("surname")
                .imageUrl("url")
                .provider(AuthProvider.APP)
                .build();
        userRepository.save(user);
    }

    @Test
    public void test_save(){
        assertEquals(1, userRepository.count());
    }

    @Test
    public void test_findByEmail(){
        Optional<User> userByEmail = userRepository.findByEmail("test@email.com");
        assertEquals("name", userByEmail.get().getName());
        assertEquals("url", userByEmail.get().getImageUrl());
    }

    @AfterEach
    private void removeTestUser() {
        userRepository.deleteAll();
    }
}
