package com.waggle.domain.projectV2;


import static com.waggle.domain.bookmark.Bookmarkable.BookmarkType.PROJECT;

import com.waggle.domain.bookmark.Bookmarkable;
import com.waggle.domain.user.entity.User;
import com.waggle.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectV2 extends BaseEntity implements Bookmarkable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column(nullable = false, length = 100)
    private String tagline;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, nullable = false, updatable = false)
    private Long sequenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    private Instant deletedAt;

    @Builder
    private ProjectV2(
        String name,
        String tagline,
        String description,
        Long sequenceId,
        User leader
    ) {
        this.name = name;
        this.tagline = tagline;
        this.description = description;
        this.sequenceId = sequenceId;
        this.leader = leader;
    }

    public void update(String name, String tagline, String description) {
        this.name = name;
        this.tagline = tagline;
        this.description = description;
    }

    public void delete() {
        this.deletedAt = Instant.now();
    }

    @Override
    public String getBookmarkableId() {
        return String.valueOf(id);
    }

    @Override
    public BookmarkType getBookmarkableType() {
        return PROJECT;
    }
}
