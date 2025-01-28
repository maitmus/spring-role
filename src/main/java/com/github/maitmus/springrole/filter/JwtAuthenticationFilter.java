package com.github.maitmus.springrole.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.maitmus.springrole.constant.Role;
import com.github.maitmus.springrole.dto.error.CommonErrorResponse;
import com.github.maitmus.springrole.exception.ForbiddenException;
import com.github.maitmus.springrole.validator.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
              throw new ForbiddenException("Not authorized");
          }

          Optional<String> accessToken = Arrays.stream(cookies)
                  .filter(cookie -> "accessToken".equals(cookie.getName()))
                  .map(Cookie::getValue)
                  .findFirst();

          Optional<String> refreshToken = Arrays.stream(cookies)
                  .filter(cookie -> "refreshToken".equals(cookie.getName()))
                  .map(Cookie::getValue)
                  .findFirst();

          if (accessToken.isPresent() && refreshToken.isPresent()) {
              Claims claims = jwtTokenValidator.validateToken(accessToken.get());

              if (claims != null) {
                  String username = claims.getSubject();
                  List<String> rolesString = claims.get("roles", List.class);
                  List<Role> roles = rolesString.stream().map(Role::valueOf).toList();

                  UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                          username,
                          null,
                          roles.stream().map(role ->
                                          new SimpleGrantedAuthority("ROLE_" + role.name()))
                                  .collect(Collectors.toList())
                  );

                  SecurityContextHolder.getContext().setAuthentication(authentication);
              }
              filterChain.doFilter(request, response);
          }
      }
      catch (Exception e) {
        log.error(e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
              new CommonErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"
              )));
      }
    }
}
