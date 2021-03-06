package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.VerificationTokenEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class VerificationTokenEntityRepositoryTest {
    @Autowired
    VerificationTokenRepository verificationTokenRepository;

    @BeforeEach
    private void saveTestVerificationToken() {
        VerificationTokenEntity token = VerificationTokenEntity.builder()
                .verified(false)
                .token("token")
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();
        verificationTokenRepository.save(token);
    }

    @Test
    public void shouldFindByToken(){
        VerificationTokenEntity token = verificationTokenRepository.findByToken("token");
        assertNotNull(token);
        assertEquals(1, verificationTokenRepository.count());
    }
}
