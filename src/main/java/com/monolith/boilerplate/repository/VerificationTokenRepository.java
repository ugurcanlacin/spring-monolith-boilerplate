package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);
    void deleteByToken(String token);
}
