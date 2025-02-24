package com.github.maitmus.springrole.dto.post;

import com.github.maitmus.springrole.dto.user.UserDetails;
import com.github.maitmus.springrole.entity.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostSummary {
    private Long id;
    private String title;
    private Integer views;
    private Integer likes;
    private Integer commentCount;
    private UserDetails author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostSummary(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.views = post.getViews();
        this.likes = post.getLikes().size();
        this.commentCount = post.getComments().size();
        this.author = new UserDetails(post.getUser());
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
