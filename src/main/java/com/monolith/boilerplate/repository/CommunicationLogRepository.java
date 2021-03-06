package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.CommunicationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationLogRepository  extends JpaRepository<CommunicationLogEntity, String> {
}
