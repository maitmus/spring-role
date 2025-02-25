package com.github.maitmus.springrole.service.post;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.constant.Role;
import com.github.maitmus.springrole.dto.post.*;
import com.github.maitmus.springrole.entity.PaginationWrapper;
import com.github.maitmus.springrole.entity.post.Comment;
import com.github.maitmus.springrole.entity.post.Dislike;
import com.github.maitmus.springrole.entity.post.Like;
import com.github.maitmus.springrole.entity.post.Post;
import com.github.maitmus.springrole.entity.user.User;
import com.github.maitmus.springrole.exception.ConflictException;
import com.github.maitmus.springrole.exception.ForbiddenException;
import com.github.maitmus.springrole.repository.comment.CommentRepository;
import com.github.maitmus.springrole.repository.post.DislikeRepository;
import com.github.maitmus.springrole.repository.post.LikeRepository;
import com.github.maitmus.springrole.repository.post.PostRepository;
import com.github.maitmus.springrole.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;

    @Transactional
    public PostCreateResponse createPost(@NotBlank String title, @NotBlank String content, Long userId) {
        User user = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));
        Post post = new Post(title, content, user);
        Post createdPost = postRepository.save(post);

        return new PostCreateResponse(createdPost.getId());
    }

    @Transactional(readOnly = true)
    public PaginationWrapper<PostSummary> getPostSummaries(Pageable pageable, String keyword) {
        Page<Post> posts = postRepository.findPostSummaries(pageable, keyword, EntityStatus.ACTIVE);
        Page<PostSummary> postSummaries = posts.map(PostSummary::new);

        return new PaginationWrapper<>(postSummaries);
    }

    @Transactional
    public CommentCreateResponse createComment(Long postId, String content, Long userId) {
        Post post = postRepository.findByIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Post not found, id: " + postId));
        User commentAuthor = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));
        Comment comment = new Comment(commentAuthor, content);
        Comment createdComment = commentRepository.save(comment);

        post.getComments().add(createdComment);

        Post updatedPost = postRepository.save(post);

        return new CommentCreateResponse(createdComment.getId(), updatedPost.getId());
    }

    @Transactional
    public PostDetail getPost(Long postId) {
        Post post = postRepository.findByIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Post not found, id: " + postId));

        post.increaseViews();

        Post updatedPost = postRepository.save(post);

        return new PostDetail(updatedPost);
    }

    @Transactional
    public PostUpdateResponse updatePost(Long postId, @NotBlank String title, @NotBlank String content, Long userId) {
        User updater = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));

        Post post = postRepository.findByIdAndUserAndStatus(postId, updater, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Post not found, id: " + postId));

        post.update(title, content);

        Post updatedPost = postRepository.save(post);

        return new PostUpdateResponse(updatedPost.getId());
    }

    @Transactional
    public PostDeleteResponse deletePost(Long postId, Long userId) {
        Post post = postRepository.findByIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Post not found, id: " + postId));

        User deleter = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));

        if (!post.getUser().getId().equals(userId) && !deleter.getRoles().contains(Role.ADMIN)) {
            throw new ForbiddenException("Permission denied, postId: " + postId + ", userId: " + userId);
        }

        post.delete();

        post.getComments().forEach(Comment::delete);

        Post deletedPost = postRepository.save(post);

        return new PostDeleteResponse(deletedPost.getId());
    }

    @Transactional
    public CommentUpdateResponse updateComment(Long commentId, String content, Long userId) {
        User author = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));
        Comment comment = commentRepository.findByIdAndUserAndStatus(commentId, author, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found, id: " + commentId));

        comment.update(content);

        Comment updatedComment = commentRepository.save(comment);

        return new CommentUpdateResponse(updatedComment.getId());
    }

    @Transactional
    public PostUpdateResponse updateLikes(Long postId, Long userId) {
        User user = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));
        Post post = postRepository.findByIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Post not found, id: " + postId));

        if (post.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Can't vote like to myself, postId: " + postId + ", userId: " + userId);
        }

        boolean isLikeAlreadyExists = likeRepository.existsByUserAndPost(user, post);

        if (isLikeAlreadyExists) {
            throw new ConflictException("Like already exists, postId: " + postId + ", userId: " + userId);
        }

        Like like = new Like(user, post);

        Like createdLike = likeRepository.save(like);

        post.increaseLikes(createdLike);

        Post updatedPost = postRepository.save(post);

        return new PostUpdateResponse(updatedPost.getId());
    }

    @Transactional
    public PostUpdateResponse updateDislikes(Long postId, Long userId) {
        User user = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));
        Post post = postRepository.findByIdAndStatus(postId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Post not found, id: " + postId));

        if (post.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Can't vote dislike to myself, postId: " + postId + ", userId: " + userId);
        }

        boolean isDislikeAlreadyExists = dislikeRepository.existsByUserAndPost(user, post);

        if (isDislikeAlreadyExists) {
            throw new ConflictException("Like already exists, postId: " + postId + ", userId: " + userId);
        }

        Dislike dislike = new Dislike(user, post);

        Dislike createdDislike = dislikeRepository.save(dislike);

        post.increaseDislikes(createdDislike);

        Post updatedPost = postRepository.save(post);

        return new PostUpdateResponse(updatedPost.getId());
    }

    @Transactional
    public CommentDeleteResponse deleteComment(Long commentId, Long userId) {
        User deleter = userRepository.findByIdAndStatus(userId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User not found, id: " + userId));

        Comment comment = commentRepository.findByIdAndStatus(commentId, EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found, id: " + commentId));

        if (!comment.getUser().getId().equals(userId) && !deleter.getRoles().contains(Role.ADMIN)) {
            throw new ForbiddenException("Permission denied, commentId: " + commentId + ", userId: " + userId);
        }

        comment.delete();

        Comment deletedComment = commentRepository.save(comment);

        return new CommentDeleteResponse(deletedComment.getId());
    }


}
