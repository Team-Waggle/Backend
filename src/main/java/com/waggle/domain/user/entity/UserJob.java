package com.waggle.domain.user.entity;

import com.waggle.domain.reference.entity.Job;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserJob {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(name = "year_cnt", nullable = false)
    private int yearCnt;
}
