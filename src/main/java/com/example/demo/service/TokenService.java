package com.example.demo.service;

import com.example.demo.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtProvider jwtProvider;

    @Value("${jwt.max-age.access}")
    private long accessTokenMaxAgeSeconds;

    @Value("${jwt.max-age.refresh}")
    private long refreshTokenMaxAgeSeconds;

    @Value("${jwt.key.access}")
    private String accessKey;

    @Value("${jwt.key.refresh}")
    private String refreshKey;

    public String createAccessToken(String subject) {
        return jwtProvider.createToken(accessKey, subject, accessTokenMaxAgeSeconds);
    }

    public String createRefreshToken(String subject) {
        return jwtProvider.createToken(refreshKey, subject, refreshTokenMaxAgeSeconds);
    }

    public boolean validateAccessToken(String token) {
        return jwtProvider.validate(accessKey, token);
    }

    public boolean validateRefreshToken(String token) {
        return jwtProvider.validate(refreshKey, token);
    }

    public String extractAccessTokenSubject(String token) {
        return jwtProvider.extractSubject(accessKey, token);
    }

    public String extractRefreshTokenSubject(String token) {
        return jwtProvider.extractSubject(refreshKey, token);
    }
}
