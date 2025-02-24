package com.github.maitmus.springrole.repository.post;

import com.github.maitmus.springrole.entity.post.Dislike;
import com.github.maitmus.springrole.entity.post.Post;
import com.github.maitmus.springrole.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {
    boolean existsByUserAndPost(User user, Post post);
}
