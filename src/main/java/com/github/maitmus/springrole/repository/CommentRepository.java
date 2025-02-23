package com.github.maitmus.springrole.repository;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.entity.post.Comment;
import com.github.maitmus.springrole.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUserAndStatus(Long id, User user, EntityStatus status);

    Optional<Comment> findByIdAndStatus(Long id, EntityStatus status);
}
