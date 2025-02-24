package com.github.maitmus.springrole.controller.auth;

import com.github.maitmus.springrole.constant.TokenType;
import com.github.maitmus.springrole.dto.auth.LoginRequest;
import com.github.maitmus.springrole.dto.auth.LoginResponse;
import com.github.maitmus.springrole.dto.auth.RegisterRequest;
import com.github.maitmus.springrole.dto.auth.RegisterResponse;
import com.github.maitmus.springrole.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "인증/인가")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse registerResponse = this.authService.register(request);
        return ResponseEntity.ok(registerResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = this.authService.login(request);
        ResponseCookie accessTokenCookie = ResponseCookie.from(TokenType.ACCESS.getValue(), loginResponse.getAccessToken())
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(TokenType.REFRESH.getValue(), loginResponse.getRefreshToken())
                .httpOnly(true)
                .sameSite("Lax")
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(loginResponse);
    }
}
