package com.waggle.domain.bookmark;

public interface Bookmarkable {

    String getBookmarkableId();

    BookmarkType getBookmarkableType();

    enum BookmarkType {
        POST, PROJECT
    }
}
