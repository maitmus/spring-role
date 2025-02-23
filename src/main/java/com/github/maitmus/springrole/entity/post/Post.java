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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Comment> comments;

    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(String title, String content, User updater) {
        this.title = title;
        this.content = content;
        this.user = updater;
    }

    public void delete() {
        this.setEntityStatus(EntityStatus.DELETED);
    }
}
