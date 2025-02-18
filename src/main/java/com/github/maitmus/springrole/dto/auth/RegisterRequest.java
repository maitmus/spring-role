package com.github.maitmus.springrole.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Schema(description = "유저 아이디")
    private String username;

    @NotBlank
    @Schema(description = "비밀번호")
    private String password;
}
