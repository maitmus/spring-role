package com.github.maitmus.springrole.dto.auth;

import com.github.maitmus.springrole.constant.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class RegisterResponse {
    private Long id;
    private String username;
    private List<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
