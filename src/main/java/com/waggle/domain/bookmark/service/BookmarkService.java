package com.waggle.domain.bookmark.service;

import com.waggle.domain.bookmark.Bookmarkable;
import com.waggle.domain.bookmark.dto.ToggleBookmarkRequest;
import com.waggle.domain.bookmark.entity.Bookmark;
import com.waggle.domain.bookmark.repository.BookmarkRepository;
import com.waggle.domain.post.Post;
import com.waggle.domain.post.repository.PostRepository;
import com.waggle.domain.projectV2.ProjectV2;
import com.waggle.domain.projectV2.repository.ProjectV2Repository;
import com.waggle.domain.user.entity.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final ProjectV2Repository projectRepository;

    @Transactional
    public boolean toggleBookmark(ToggleBookmarkRequest toggleBookmarkRequest, User user) {
        if (bookmarkRepository.existsByBookmarkableIdAndBookmarkTypeAndUserId(
            toggleBookmarkRequest.bookmarkableId(),
            toggleBookmarkRequest.bookmarkType(),
            user.getId()
        )) {
            bookmarkRepository.deleteByBookmarkableIdAndBookmarkTypeAndUserId(
                toggleBookmarkRequest.bookmarkableId(),
                toggleBookmarkRequest.bookmarkType(),
                user.getId());
            return false;
        } else {
            Bookmark bookmark = Bookmark.builder()
                .bookmarkableId(toggleBookmarkRequest.bookmarkableId())
                .bookmarkType(toggleBookmarkRequest.bookmarkType())
                .user(user)
                .build();
            bookmarkRepository.save(bookmark);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public List<Bookmarkable> getUserBookmarks(User user) {
        List<Bookmark> bookmarks = bookmarkRepository.findByUserIdOrderByCreatedAtDesc(
            user.getId()
        );

        List<Long> postIds = new ArrayList<>();
        List<UUID> projectIds = new ArrayList<>();

        for (Bookmark bookmark : bookmarks) {
            switch (bookmark.getBookmarkType()) {
                case POST -> postIds.add(Long.valueOf(bookmark.getBookmarkableId()));
                case PROJECT -> projectIds.add(UUID.fromString(bookmark.getBookmarkableId()));
            }
        }

        Map<String, Bookmarkable> bookmarkableMap = new HashMap<>();

        if (!postIds.isEmpty()) {
            postRepository.findAllByIdInWithRelations(postIds)
                .forEach(post -> bookmarkableMap.put(String.valueOf(post.getId()), post));
        }

        if (!projectIds.isEmpty()) {
            projectRepository.findAllByIdInWithRelations(projectIds)
                .forEach(project -> bookmarkableMap.put(project.getId().toString(), project));
        }

        return bookmarks.stream()
            .map(bookmark -> bookmarkableMap.get(bookmark.getBookmarkableId()))
            .filter(Objects::nonNull)
            .sorted((a, b) -> {
                Instant aCreatedAt = a instanceof Post ?
                    ((Post) a).getCreatedAt() : ((ProjectV2) a).getCreatedAt();
                Instant bCreatedAt = b instanceof Post ?
                    ((Post) b).getCreatedAt() : ((ProjectV2) b).getCreatedAt();

                int result = bCreatedAt.compareTo(aCreatedAt);

                if (result == 0) {
                    if (a instanceof Post && b instanceof Post) {
                        return Long.compare(((Post) b).getId(), ((Post) a).getId());
                    } else if (a instanceof ProjectV2 && b instanceof ProjectV2) {
                        return ((ProjectV2) b).getSequenceId()
                            .compareTo(((ProjectV2) a).getSequenceId());
                    } else {
                        return a instanceof Post ? -1 : 1;
                    }
                }

                return result;
            })
            .toList();
    }
}
