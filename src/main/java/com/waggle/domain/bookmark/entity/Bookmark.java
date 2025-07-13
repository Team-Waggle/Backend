package com.waggle.domain.bookmark.entity;

import com.waggle.domain.bookmark.Bookmarkable.BookmarkType;
import com.waggle.domain.user.entity.User;
import com.waggle.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"bookmarkable_id", "bookmark_type", "user_id"}
    )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String bookmarkableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private BookmarkType bookmarkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Builder
    public Bookmark(String bookmarkableId, BookmarkType bookmarkType, User user) {
        this.bookmarkableId = bookmarkableId;
        this.bookmarkType = bookmarkType;
        this.user = user;
    }
}
