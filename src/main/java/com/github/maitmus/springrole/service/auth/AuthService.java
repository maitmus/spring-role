package com.github.maitmus.springrole.service.auth;

import com.github.maitmus.springrole.constant.EntityStatus;
import com.github.maitmus.springrole.constant.TokenType;
import com.github.maitmus.springrole.dto.auth.LoginRequest;
import com.github.maitmus.springrole.dto.auth.LoginResponse;
import com.github.maitmus.springrole.dto.auth.RegisterRequest;
import com.github.maitmus.springrole.dto.auth.RegisterResponse;
import com.github.maitmus.springrole.entity.user.User;
import com.github.maitmus.springrole.exception.NotFoundException;
import com.github.maitmus.springrole.exception.UnauthorizedException;
import com.github.maitmus.springrole.repository.user.UserRepository;
import com.github.maitmus.springrole.validator.JwtTokenValidator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class AuthService {
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 3600 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 3600 * 1000 * 24 * 7L;
    private final UserRepository userRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       JwtTokenValidator jwtTokenValidator,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenValidator = jwtTokenValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(@Valid RegisterRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = this.userRepository.save(
                new User(
                        request.getUsername(),
                        hashedPassword,
                        EntityStatus.ACTIVE
                )
        );

        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

    }

    @Transactional(readOnly = true)
    public LoginResponse login(@Valid LoginRequest request) {
        User user = this.userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("User not found, username: " + request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid password");
        }

        Map<String, Object> claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("roles", user.getRoles());
        claims.put("status", user.getStatus());
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accessToken(generateToken(user.getUsername(), claims, TokenType.ACCESS))
                .refreshToken(generateToken(user.getUsername(), claims, TokenType.REFRESH))
                .build();
    }

    private String generateToken(String username, Map<String, Object> claims, TokenType type) {
        Date now = new Date();

        Date expiryDate;

        if (type.equals(TokenType.ACCESS)) {
            expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        } else if (type.equals(TokenType.REFRESH)) {
            expiryDate = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(jwtTokenValidator.getSigningKey(), SignatureAlgorithm.HS256)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .compact();
    }


}
