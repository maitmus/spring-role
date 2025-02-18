package com.github.maitmus.springrole.dto.user;

import com.github.maitmus.springrole.constant.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateUserRoleRequest {
    @NotEmpty
    @Schema(description = "역할")
    private List<Role> roles;
}
