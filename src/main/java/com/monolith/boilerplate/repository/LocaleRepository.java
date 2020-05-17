package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.LocaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocaleRepository extends JpaRepository<LocaleEntity, String> {

}
