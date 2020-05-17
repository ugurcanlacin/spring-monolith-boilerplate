package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.VerificationTokenEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationTokenEntity, String> {
    VerificationTokenEntity findByToken(String token);
    void deleteByToken(String token);

    @Modifying
    @Query("UPDATE VerificationTokenEntity t SET t.verified = true WHERE t.token = :token")
    void verify(@Param("token") String token);
}
