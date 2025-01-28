package com.github.maitmus.springrole.service;

import com.github.maitmus.springrole.dto.auth.LoginRequest;
import com.github.maitmus.springrole.dto.auth.LoginResponse;
import com.github.maitmus.springrole.dto.auth.RegisterRequest;
import com.github.maitmus.springrole.dto.auth.RegisterResponse;
import com.github.maitmus.springrole.entity.User;
import com.github.maitmus.springrole.exception.NotFoundException;
import com.github.maitmus.springrole.exception.UnauthorizedException;
import com.github.maitmus.springrole.repository.UsersRepository;
import com.github.maitmus.springrole.validator.JwtTokenValidator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class AuthService {
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 3600 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 3600 * 1000 * 24 * 7L;
    private final UsersRepository userRepository;
    private final JwtTokenValidator jwtTokenValidator;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsersRepository userRepository,
                       JwtTokenValidator jwtTokenValidator,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenValidator = jwtTokenValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse register(@Valid RegisterRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = this.userRepository.save(
                new User(
                        request.getUsername(),
                        hashedPassword
                )
        );

        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

    }

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
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .accessToken(generateToken(user.getUsername(), claims, "ACCESS"))
                .refreshToken(generateToken(user.getUsername(), claims, "REFRESH"))
                .build();
    }

    private String generateToken(String username, Map<String, Object> claims, String type) {
        Date now = new Date();

        Date expiryDate;

        if (type.equals("ACCESS")) {
            expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);
        } else if (type.equals("REFRESH")) {
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
