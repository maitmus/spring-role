package com.github.maitmus.springrole.repository.post;

import com.github.maitmus.springrole.entity.post.Like;
import com.github.maitmus.springrole.entity.post.Post;
import com.github.maitmus.springrole.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
}
