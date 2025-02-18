package com.github.maitmus.springrole.controller;

import com.github.maitmus.springrole.constant.TokenType;
import com.github.maitmus.springrole.dto.user.DeleteCurrentUserResponse;
import com.github.maitmus.springrole.dto.user.UpdateUserRoleRequest;
import com.github.maitmus.springrole.dto.user.UserDetails;
import com.github.maitmus.springrole.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserDetails getCurrentUser(@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetails getUser(@PathVariable Long id) {
        return this.userService.getUser(id);
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetails updateUserRole(@PathVariable Long userId,
                                      @RequestBody @Valid UpdateUserRoleRequest request) {
        return this.userService.updateUserRole(userId, request.getRoles());
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DeleteCurrentUserResponse> deleteCurrentUser(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails
    ) {
        DeleteCurrentUserResponse response = this.userService.deleteCurrentUser(userDetails.getId());

        ResponseCookie accessTokenCookie = ResponseCookie.from(TokenType.ACCESS.getValue(), "")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from(TokenType.REFRESH.getValue(), "")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }
}
