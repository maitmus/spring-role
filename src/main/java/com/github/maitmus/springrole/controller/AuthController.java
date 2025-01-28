package com.github.maitmus.springrole.controller;

import com.github.maitmus.springrole.dto.auth.LoginRequest;
import com.github.maitmus.springrole.dto.auth.LoginResponse;
import com.github.maitmus.springrole.dto.auth.RegisterRequest;
import com.github.maitmus.springrole.dto.auth.RegisterResponse;
import com.github.maitmus.springrole.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
        Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.getAccessToken());
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok(loginResponse);
    }
}
