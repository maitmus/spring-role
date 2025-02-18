package com.github.maitmus.springrole.dto.user;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.constant.Role;
import com.github.maitmus.springrole.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private Long id;
    @Schema(description = "유저 아이디")
    private String username;
    @Schema(description = "역할")
    private List<Role> roles;
    @Schema(description = "계정 활성화 여부")
    private EntityStatus status;

    public UserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
        this.status = user.getStatus();
    }
}
