package com.waggle.domain.bookmark.repository;

import com.waggle.domain.bookmark.Bookmarkable.BookmarkType;
import com.waggle.domain.bookmark.entity.Bookmark;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByBookmarkableIdAndBookmarkTypeAndUserId(
        String bookmarkableId,
        BookmarkType bookmarkType,
        UUID userId
    );

    Optional<Bookmark> findByBookmarkableIdAndUserId(String bookmarkableId, UUID userId);

    List<Bookmark> findByUserIdOrderByCreatedAtDesc(UUID userId);

    void deleteByBookmarkableIdAndBookmarkTypeAndUserId(
        String bookmarkableId,
        BookmarkType bookmarkType,
        UUID userId
    );
}
