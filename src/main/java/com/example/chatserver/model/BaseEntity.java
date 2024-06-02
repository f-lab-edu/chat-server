package com.example.chatserver.model;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public class BaseEntity {

    private Timestamp createdAt;
    private Timestamp modifiedAt;

    @PrePersist
    public void prePersist(){
        ZonedDateTime kst = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.createdAt = Timestamp.valueOf(kst.toLocalDateTime());
        this.modifiedAt = Timestamp.valueOf(kst.toLocalDateTime());
    }
    @PreUpdate
    public void preUpdate(){
        ZonedDateTime kst = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        this.modifiedAt = Timestamp.valueOf(kst.toLocalDateTime());
    }
}
