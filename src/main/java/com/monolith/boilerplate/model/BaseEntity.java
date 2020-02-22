package com.monolith.boilerplate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @LastModifiedBy
    @Column
    private String lastModifiedBy;

    @Version
    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP(3) NULL DEFAULT NULL")
    private LocalDateTime lastModifiedAt;

    @CreatedBy
    @Column
    private String createdBy;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP(3) NULL DEFAULT NULL")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        isDeleted = false;
    }

}