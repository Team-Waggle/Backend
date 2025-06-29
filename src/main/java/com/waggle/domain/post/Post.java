package com.waggle.domain.post;

import static com.waggle.domain.bookmark.Bookmarkable.BookmarkType.POST;

import com.waggle.domain.bookmark.Bookmarkable;
import com.waggle.domain.projectV2.ProjectV2;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity implements Bookmarkable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectV2 project;

    private Instant deletedAt;

    @Builder
    private Post(String title, String content, User user, ProjectV2 project) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.project = project;
    }

    public void update(String title, String content, ProjectV2 project) {
        this.title = title;
        this.content = content;
        this.project = project;
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
        return POST;
    }

}
