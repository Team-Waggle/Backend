package com.waggle.global.response.swagger;

import com.waggle.domain.post.dto.PostResponse;
import com.waggle.global.response.SuccessResponse;
import java.util.List;

public class PostsSuccessResponse extends SuccessResponse<List<PostResponse>> {

    public PostsSuccessResponse(int code, String message, List<PostResponse> payload) {
        super(code, message, payload);
    }
}
