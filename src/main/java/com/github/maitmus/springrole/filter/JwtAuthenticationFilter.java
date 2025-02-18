package com.github.maitmus.springrole.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.constant.Role;
import com.github.maitmus.springrole.constant.TokenType;
import com.github.maitmus.springrole.dto.error.CommonErrorResponse;
import com.github.maitmus.springrole.dto.user.UserDetails;
import com.github.maitmus.springrole.validator.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenValidator jwtTokenValidator;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePath = {"/swagger-ui/**", "/swagger-ui/index.html"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws IOException {
        try {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                throw new Exception("Cookie not found");
            }

            Optional<String> accessToken = Arrays.stream(cookies)
                    .filter(cookie -> TokenType.ACCESS.getValue().equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();

            Optional<String> refreshToken = Arrays.stream(cookies)
                    .filter(cookie -> TokenType.REFRESH.getValue().equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();

            if (!(accessToken.isPresent() || refreshToken.isPresent())) {
                throw new Exception("Token not found");
            }

            Claims claims = jwtTokenValidator.validateToken(accessToken.get());

            if (claims != null) {
                String username = claims.getSubject();
                Long id = claims.get("id", Long.class);
                List<?> rawRoles = claims.get("roles", List.class);
                String statusString = claims.get("status", String.class);
                EntityStatus status = EntityStatus.valueOf(statusString);
                List<String> rolesString = rawRoles.stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .toList();
                List<Role> roles = rolesString.stream().map(Role::valueOf).toList();

                UserDetails userDetails = new UserDetails(id, username, roles, status);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        roles.stream().map(role ->
                                        new SimpleGrantedAuthority("ROLE_" + role.name()))
                                .collect(Collectors.toList())
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(
                    new CommonErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"
                    )));
        }
    }
}
