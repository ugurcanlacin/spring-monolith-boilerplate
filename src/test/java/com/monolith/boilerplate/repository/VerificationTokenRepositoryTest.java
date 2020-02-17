package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.Privilege;
import com.monolith.boilerplate.model.Role;
import com.monolith.boilerplate.model.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class VerificationTokenRepositoryTest {
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @BeforeEach
    private void saveTestVerificationToken() {
        VerificationToken token = VerificationToken.builder()
                .isVerified(false)
                .token("token")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
        verificationTokenRepository.save(token);
    }

    @Test
    public void shouldFindByToken(){
        VerificationToken token = verificationTokenRepository.findByToken("token");
        assertNotNull(token);
        assertEquals(1, verificationTokenRepository.count());
    }
}
