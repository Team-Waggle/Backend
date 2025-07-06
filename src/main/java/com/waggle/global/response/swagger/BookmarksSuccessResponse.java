package com.waggle.global.response.swagger;

import com.waggle.domain.bookmark.dto.BookmarkResponse;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class BookmarksSuccessResponse extends SuccessResponse<List<BookmarkResponse>> {

    public BookmarksSuccessResponse(
        int code,
        String message,
        List<BookmarkResponse> payload
    ) {
        super(code, message, payload);
    }
}
