package com.waggle.domain.bookmark.controller;

import com.waggle.domain.bookmark.dto.ToggleBookmarkRequest;
import com.waggle.domain.bookmark.service.BookmarkService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.security.oauth2.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v2/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<BaseResponse<Boolean>> toggleBookmark(
        @Valid @RequestBody ToggleBookmarkRequest toggleBookmarkRequest,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return SuccessResponse.of(
            ApiStatus._OK,
            bookmarkService.toggleBookmark(toggleBookmarkRequest, userPrincipal.getUser())
        );
    }
}
