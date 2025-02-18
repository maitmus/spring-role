package com.github.maitmus.springrole.dto.user;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.constant.Role;
import com.github.maitmus.springrole.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private Long id;
    private String username;
    private List<Role> roles;
    private EntityStatus status;

    public UserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
        this.status = user.getStatus();
    }
}
