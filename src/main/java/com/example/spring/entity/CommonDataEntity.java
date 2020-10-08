package com.example.spring.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonDataEntity { // 날짜 필드 상속 처리

    @CreatedDate // Entity 생성시 자동으로 날짜 세팅
    private LocalDateTime createdAt;

    @LastModifiedDate // Entity 수정 시 자동으로 날짜 세팅
    private LocalDateTime modifiedAt;

}
