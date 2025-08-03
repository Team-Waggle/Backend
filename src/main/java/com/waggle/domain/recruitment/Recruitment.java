package com.waggle.domain.recruitment;

import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.reference.enums.Position;
import com.waggle.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Position position;

    @Column(nullable = false)
    private int remainingCount;

    @Column(nullable = false)
    private int currentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectV2 project;

    public boolean isRecruitable() {
        return remainingCount > 0;
    }

    public void addMember() {
        if (remainingCount <= 0) {
            throw new IllegalStateException("No remaining positions for position: " + position);
        }
        remainingCount--;
        currentCount++;
    }

    public void removeMember() {
        if (currentCount <= 0) {
            throw new IllegalStateException("No current positions for position: " + position);
        }
        remainingCount++;
        currentCount--;
    }
}
