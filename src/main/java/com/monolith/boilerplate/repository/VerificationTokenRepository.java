package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);
    void deleteByToken(String token);

    @Modifying
    @Query("UPDATE VerificationToken t SET t.isVerified = true WHERE t.token = :token")
    void verify(@Param("token") String token);
}
