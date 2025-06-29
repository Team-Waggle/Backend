package com.waggle.domain.bookmark.controller;

import com.waggle.domain.bookmark.dto.ToggleBookmarkRequest;
import com.waggle.domain.bookmark.service.BookmarkService;
import com.waggle.global.response.BaseResponse;
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
@RequestMapping("/api/v2/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> toggleBookmark(
        @Valid @RequestBody ToggleBookmarkRequest request,
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return null;
    }
}
