package com.github.maitmus.springrole.dto.user;

import com.github.maitmus.springrole.constant.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateUserRoleRequest {
    @NotEmpty
    private List<Role> roles;
}
