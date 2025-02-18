package com.github.maitmus.springrole.entity;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public User(String username, String password, EntityStatus status) {
        this.username = username;
        this.password = password;
        roles.add(Role.USER);

        this.setEntityStatus(status);
    }

    public void updateRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void delete() {
        this.setEntityStatus(EntityStatus.DELETED);
    }
}
