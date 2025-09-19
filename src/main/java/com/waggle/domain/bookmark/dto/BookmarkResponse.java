package com.waggle.domain.bookmark.dto;

import static com.waggle.domain.bookmark.Bookmarkable.BookmarkType.POST;
import static com.waggle.domain.bookmark.Bookmarkable.BookmarkType.PROJECT;

import com.waggle.domain.bookmark.Bookmarkable;
import com.waggle.domain.bookmark.Bookmarkable.BookmarkType;
import com.waggle.domain.post.Post;
import com.waggle.domain.projectV2.ProjectV2;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "북마크 응답 DTO")
public record BookmarkResponse(
    @Schema(description = "북마크 대상 ID")
    String targetId,

    @Schema(description = "북마크 타입", example = "PROJECT", allowableValues = {"POST", "PROJECT"})
    BookmarkType bookmarkType,

    @Schema(description = "북마크 객체")
    Object target,

    @Schema(description = "북마크 시간", example = "2025-06-08T10:30:00Z")
    LocalDateTime bookmarkedAt
) {

    public static BookmarkResponse from(Bookmarkable bookmarkable) {
        if (bookmarkable instanceof Post post) {
            return new BookmarkResponse(
                String.valueOf(post.getId()),
                POST,
                post,
                post.getCreatedAt()
            );
        } else if (bookmarkable instanceof ProjectV2 project) {
            return new BookmarkResponse(
                project.getId().toString(),
                PROJECT,
                project,
                project.getCreatedAt()
            );
        } else {
            throw new IllegalArgumentException("Unsupported bookmarkable object");
        }
    }
}
