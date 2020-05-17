package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.TextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends JpaRepository<TextEntity, String> {
    String findByTextKey(String textKey);
}
