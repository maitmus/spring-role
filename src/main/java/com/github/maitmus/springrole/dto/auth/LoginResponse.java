package com.github.maitmus.springrole.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@NoArgsConstructor
@SuperBuilder
public class LoginResponse extends RegisterResponse {
    private String accessToken;
    private String refreshToken;
}
