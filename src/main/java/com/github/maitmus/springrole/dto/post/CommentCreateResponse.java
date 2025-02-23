package com.github.maitmus.springrole.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateResponse {
    private Long commentId;
    private Long postId;
}
