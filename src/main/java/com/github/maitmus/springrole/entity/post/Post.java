package com.github.maitmus.springrole.entity.post;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.entity.BaseEntity;
import com.github.maitmus.springrole.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Table(name = "posts")
@NoArgsConstructor
public class Post extends BaseEntity {
    @Column
    private String title;

    @Column
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private Integer views;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Like> likes;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Dislike> dislikes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Comment> comments;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.views = 0;
    }

    public void increaseViews() {
        this.views++;
    }

    public void increaseLikes(Like like) {
        this.likes.add(like);
    }

    public void increaseDislikes(Dislike dislike) {
        this.dislikes.add(dislike);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void delete() {
        this.setEntityStatus(EntityStatus.DELETED);
    }
}
