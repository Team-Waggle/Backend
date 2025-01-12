package com.waggle.domain.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter //getter: 값을 가져올 수 있게 해주는 것, setter: 값을 수정할 수 있게 해주는 것
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title; //제목

    @Column(name = "recruitment_date", nullable = false)
    private LocalDateTime recruitmentDate; //프로젝트 모집 마감 일자

    @Column(length = 1000)
    private String detail; //소개

    @Column(name = "connect_url")
    private String connectUrl; //연락 링크

    @Column(name = "reference_url")
    private String referenceUrl; //참고 링크

    @Column(name = "bookmark_cnt")
    private int bookmarkCnt; //북마크 수(스크랩)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; //생성일자

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; //수정일자
}
