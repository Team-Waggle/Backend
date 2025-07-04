package com.waggle.domain.bookmark.dto;

import com.waggle.domain.bookmark.Bookmarkable.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "북마크 토글 요청 DTO")
public record ToggleBookmarkRequest(
    @Schema(description = "북마크 객체 ID", example = "42")
    String bookmarkableId,

    @Schema(description = "북마크 타입", example = "POST")
    BookmarkType bookmarkType
) {

}
