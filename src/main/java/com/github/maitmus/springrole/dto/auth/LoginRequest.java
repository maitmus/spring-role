package com.github.maitmus.springrole.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Schema(description = "유저 아이디")
    private String username;
    @Schema(description = "비밀번호")
    private String password;
}
