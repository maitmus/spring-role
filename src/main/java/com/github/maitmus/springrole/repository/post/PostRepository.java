package com.github.maitmus.springrole.repository.post;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.entity.post.Post;
import com.github.maitmus.springrole.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p " +
            "where (:keyword is null or p.title like %:keyword%)" +
            "and p.status = :status " +
            "order by p.id desc")
    Page<Post> findPostSummaries(Pageable pageable, String keyword, EntityStatus status);

    Optional<Post> findByIdAndStatus(Long id, EntityStatus status);

    Optional<Post> findByIdAndUserAndStatus(Long id, User user, EntityStatus status);
}
