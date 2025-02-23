package com.github.maitmus.springrole.dto.post;

import com.github.maitmus.springrole.dto.user.UserDetails;
import com.github.maitmus.springrole.entity.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostDetail {
    private Long id;
    private String title;
    private String content;
    private List<CommentDetail> comments;
    private UserDetails author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostDetail(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.comments = post.getComments().stream().map(CommentDetail::new).collect(Collectors.toList());
        this.author = new UserDetails(post.getUser());
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
