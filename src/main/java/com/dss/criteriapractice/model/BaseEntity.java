package com.dss.criteriapractice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;

    protected boolean isActive;

    @LastModifiedDate
    protected LocalDateTime modifiedAt;

    @LastModifiedBy
    protected String modifiedBy;


    @PrePersist
    public void prePersist() {
        isActive = true;
        modifiedAt = LocalDateTime.now();
        modifiedBy = "system";
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
