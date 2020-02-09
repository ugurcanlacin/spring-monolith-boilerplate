package com.monolith.boilerplate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BaseEntity {
    @Column(insertable = true, nullable = false, updatable = false)
    private Date creationDate;
    @Column(insertable = false, nullable = true, updatable = true)
    private Date updateDate;
    @Column(insertable = true, nullable = true, updatable = true)
    private Boolean deleted;

    @PrePersist
    protected void onCreate() {
        deleted = false;
        creationDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = new Date();
    }
}