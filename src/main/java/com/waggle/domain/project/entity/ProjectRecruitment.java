package com.waggle.domain.project.entity;

import com.waggle.domain.reference.enums.JobRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectRecruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "job_role", nullable = false)
    private JobRole jobRole;

    @Column(name = "remaining_count", nullable = false)
    private int remainingCount;

    @Column(name = "current_count", nullable = false)
    private int currentCount;

    public boolean isRecruitable() {
        return remainingCount > 0;
    }

    public void addMember() {
        if (remainingCount <= 0) {
            throw new IllegalStateException("No remaining positions for job role: " + jobRole);
        }
        remainingCount--;
        currentCount++;
    }

    public void removeMember() {
        if (currentCount <= 0) {
            throw new IllegalStateException("No current positions for job role: " + jobRole);
        }
        remainingCount++;
        currentCount--;
    }
}
