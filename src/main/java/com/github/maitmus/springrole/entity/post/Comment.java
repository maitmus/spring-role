package com.github.maitmus.springrole.entity.post;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.entity.BaseEntity;
import com.github.maitmus.springrole.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column
    private String content;

    public Comment(User author, String content) {
        this.user = author;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

    public void delete() {
        this.setEntityStatus(EntityStatus.DELETED);
    }
}
