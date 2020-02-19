package com.monolith.boilerplate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Version
    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP(3) NULL DEFAULT NULL")
    private LocalDateTime lastModifiedAt;
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP(3) NULL DEFAULT NULL")
    private LocalDateTime createdAt;

    @Column(insertable = true, nullable = true, updatable = true)
    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        isDeleted = false;
    }
}