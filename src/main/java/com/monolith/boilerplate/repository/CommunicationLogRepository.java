package com.monolith.boilerplate.repository;

import com.monolith.boilerplate.model.CommunicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunicationLogRepository  extends JpaRepository<CommunicationLog, String> {
}
