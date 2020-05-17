package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<TranslationEntity, String> {
}
