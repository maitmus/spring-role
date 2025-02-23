package com.github.maitmus.springrole.dto.post;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.dto.user.UserDetails;
import com.github.maitmus.springrole.entity.post.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDetail {
    private Long id;
    private UserDetails author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EntityStatus status;

    public CommentDetail(Comment comment) {
        this.id = comment.getId();
        // 삭제된 댓글들의 저자와 내용은 조회하지 못하도록 막기ㄴ
        this.author = comment.getStatus() == EntityStatus.ACTIVE ? new UserDetails(comment.getUser()) : null;
        this.content = comment.getStatus() == EntityStatus.ACTIVE ? comment.getContent() : null;
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.status = comment.getStatus();
    }
}
