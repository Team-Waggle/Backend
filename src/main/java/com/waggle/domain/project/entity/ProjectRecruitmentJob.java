package com.waggle.domain.project.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.entity.Job;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRecruitmentJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    //BackReference가 무한 반복 끊어내는거고, Ignore는 실제로는 안에 데이터가 있지만 Json에서만 안 보여줌.
    //중간 테이블에는 JsonBackReference를 해줘야 함.
    @JsonBackReference
    @JsonIgnore
    private Project project;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @JsonProperty("job")
    private Job job;

    @Column(name = "recruitment_cnt", nullable = false, columnDefinition = "integer default 0")
    @JsonProperty("recruitment_cnt")
    private int recruitmentCnt;
}