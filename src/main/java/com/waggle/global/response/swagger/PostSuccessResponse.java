package com.waggle.global.response.swagger;

import com.waggle.domain.post.dto.PostResponse;
import com.waggle.global.response.SuccessResponse;

public class PostSuccessResponse extends SuccessResponse<PostResponse> {

    public PostSuccessResponse(int code, String message, PostResponse payload) {
        super(code, message, payload);
    }
}
