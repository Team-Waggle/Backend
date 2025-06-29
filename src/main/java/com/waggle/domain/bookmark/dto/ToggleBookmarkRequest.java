package com.waggle.domain.bookmark.dto;

import com.waggle.domain.bookmark.Bookmarkable.BookmarkType;

public record ToggleBookmarkRequest(
    String targetId,
    BookmarkType bookmarkType
) {

}
