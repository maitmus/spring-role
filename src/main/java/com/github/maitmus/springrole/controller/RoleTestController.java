package com.github.maitmus.springrole.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@Tag(name = "역할 테스팅 용 컨트롤러")
public class RoleTestController {
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "유저 전용 엔드포인트")
    public String testRoleUser() {
        return "Request from user";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "관리자 전용 엔드포인트")
    public String testRoleAdmin() {
        return "Request from admin";
    }
}
