package com.github.maitmus.springrole.entity.post;

import com.github.maitmus.springrole.entity.BaseEntity;
import com.github.maitmus.springrole.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@AllArgsConstructor
@NoArgsConstructor
public class Like extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
