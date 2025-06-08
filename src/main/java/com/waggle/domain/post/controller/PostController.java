package com.waggle.domain.post.controller;

import com.waggle.domain.post.Post;
import com.waggle.domain.post.dto.PostResponse;
import com.waggle.domain.post.dto.UpsertPostDto;
import com.waggle.domain.post.service.PostService;
import com.waggle.global.response.ApiStatus;
import com.waggle.global.response.BaseResponse;
import com.waggle.global.response.ErrorResponse;
import com.waggle.global.response.SuccessResponse;
import com.waggle.global.response.swagger.EmptySuccessResponse;
import com.waggle.global.response.swagger.PostSuccessResponse;
import com.waggle.global.response.swagger.PostsSuccessResponse;
import com.waggle.global.secure.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v2/posts")
@RestController
public class PostController {

    private final PostService postService;

    @Operation(
        summary = "게시글 생성",
        description = "새로운 게시글을 생성합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "게시글 생성 성공",
            content = @Content(schema = @Schema(implementation = PostSuccessResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 프로젝트",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    public ResponseEntity<BaseResponse<PostResponse>> createPost(
        @Valid @RequestBody UpsertPostDto upsertPostDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Post post = postService.createPost(upsertPostDto, userDetails.getUser());

        return SuccessResponse.of(
            ApiStatus._CREATED,
            PostResponse.from(post)
        );
    }

    @Operation(
        summary = "게시글 목록 조회",
        description = "게시글 목록을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "게시글 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = PostsSuccessResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<PostResponse>>> getPosts() {
        List<Post> posts = postService.getPosts();

        return SuccessResponse.of(
            ApiStatus._OK,
            posts.stream().map(PostResponse::from).toList()
        );
    }

    @Operation(
        summary = "게시글 조회",
        description = "게시글을 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "게시글 조회 성공",
            content = @Content(schema = @Schema(implementation = PostSuccessResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 게시글",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(@PathVariable Long postId) {
        Post post = postService.getPost(postId);

        return SuccessResponse.of(
            ApiStatus._OK,
            PostResponse.from(post)
        );
    }

    @Operation(
        summary = "게시글 수정",
        description = "게시글을 수정합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "게시글 수정 성공",
            content = @Content(schema = @Schema(implementation = PostSuccessResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 리소스",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "존재하지 않는 게시글",
                        description = "수정하려는 게시글이 존재하지 않는 경우",
                        value = """
                            {
                                "isSuccess": false,
                                "code": 404,
                                "message": "Post not found with id: 999",
                                "payload": null,
                                "timestamp": "2025-06-08T10:30:00Z"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "존재하지 않는 프로젝트",
                        description = "연결하려는 프로젝트가 존재하지 않는 경우",
                        value = """
                            {
                                "isSuccess": false,
                                "code": 404,
                                "message": "Project not found with id: 999",
                                "payload": null,
                                "timestamp": "2025-06-08T10:30:00Z"
                            }
                            """
                    )
                }
            )
        )
    })
    @PatchMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponse>> updatePost(
        @PathVariable Long postId,
        @Valid @RequestBody UpsertPostDto upsertPostDto,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Post post = postService.updatePost(postId, upsertPostDto, userDetails.getUser());

        return SuccessResponse.of(
            ApiStatus._OK,
            PostResponse.from(post)
        );
    }

    @Operation(
        summary = "게시글 삭제",
        description = "게시글을 삭제합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "게시글 삭제 성공",
            content = @Content(schema = @Schema(implementation = EmptySuccessResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증되지 않은 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 게시글",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        postService.deletePost(postId, userDetails.getUser());

        return SuccessResponse.of(
            ApiStatus._NO_CONTENT,
            null
        );
    }
}
