package com.github.maitmus.springrole.controller.post;

import com.github.maitmus.springrole.dto.post.*;
import com.github.maitmus.springrole.dto.user.UserDetails;
import com.github.maitmus.springrole.entity.PaginationWrapper;
import com.github.maitmus.springrole.service.post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "게시물 작성")
    public PostCreateResponse createPost(@RequestBody @Valid PostCreateRequest request,
                                         @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return this.postService.createPost(request.getTitle(), request.getContent(), userDetails.getId());
    }

    @PostMapping("/{post-id}/comment")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "댓글 작성")
    public CommentCreateResponse createComment(@PathVariable(name = "post-id") Long postId,
                                               @RequestBody @Valid CommentCreateRequest request,
                                               @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return this.postService.createComment(postId, request.getContent(), userDetails.getId());
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "게시물 조회(리스트)")
    public PaginationWrapper<PostSummary> getPosts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return this.postService.getPostSummaries(pageable, keyword);
    }

    @GetMapping("/{post-id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "게시물 조회(상세)")
    public PostDetail getPost(@PathVariable(name = "post-id") Long postId) {
        return this.postService.getPost(postId);
    }

    @PutMapping("/{post-id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "게시물 수정")
    public PostUpdateResponse updatePost(
            @PathVariable(name = "post-id") Long postId,
            @RequestBody @Valid PostUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return this.postService.updatePost(postId, request.getTitle(), request.getContent(), userDetails.getId());
    }

    @PutMapping("/comment")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "댓글 수정")
    public CommentUpdateResponse updateComment(
            @RequestParam Long id,
            @RequestBody @Valid CommentUpdateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        return this.postService.updateComment(id, request.getContent(), userDetails.getId());
    }

    @DeleteMapping("/{post-id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "게시물 삭제",
            description = "게시물 작성자/운영자 모두 삭제 가능")
    public PostDeleteResponse deletePost(@PathVariable(name = "post-id") Long postId,
                                         @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return this.postService.deletePost(postId, userDetails.getId());
    }

    @DeleteMapping("/comment/{comment-id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "댓글 삭제",
            description = "댓글 작성자/운영자 모두 삭제 가능")
    public CommentDeleteResponse deleteComment(
            @PathVariable(name = "comment-id") Long commentId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        return this.postService.deleteComment(commentId, userDetails.getId());
    }
}
