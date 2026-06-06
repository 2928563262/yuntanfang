package com.yuntanfang.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.expire-seconds:7200}")
    private long expireSeconds;

    public String createToken(String subject, String role) {
        String payload = subject + ":" + role + ":" + Instant.now().plusSeconds(expireSeconds).getEpochSecond();
        return Base64.getUrlEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, Object> loginPayload(String subject, String role) {
        return Map.of("token", createToken(subject, role), "tokenType", "Bearer", "role", role);
    }
}
