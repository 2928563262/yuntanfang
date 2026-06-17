package com.yuntanfang.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * 从请求的 Authorization 头解析当前登录用户。
 * 约定 token 为 Base64Url("subject:role:exp")，subject 为用户主键 id（登录时写入）。
 */
@Component
public class AuthSupport {

    public Long currentUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authHeader.substring(7).trim();
            String decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String subject = decoded.split(":")[0];
            return Long.parseLong(subject);
        } catch (Exception ex) {
            return null;
        }
    }

    public String currentRole(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            String token = authHeader.substring(7).trim();
            String decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
            String[] parts = decoded.split(":");
            return parts.length > 1 ? parts[1] : null;
        } catch (Exception ex) {
            return null;
        }
    }

    public Long requireUserId(String authHeader) {
        Long userId = currentUserId(authHeader);
        if (userId == null) {
            throw new AuthenticationCredentialsNotFoundException("未认证");
        }
        return userId;
    }

    public String requireAnyRole(String authHeader, String... roles) {
        String role = currentRole(authHeader);
        if (role == null) {
            throw new AuthenticationCredentialsNotFoundException("未认证");
        }
        if (Arrays.stream(roles).noneMatch(role::equals)) {
            throw new AccessDeniedException("无权限");
        }
        return role;
    }

    public Long requireUserIdWithRole(String authHeader, String... roles) {
        requireAnyRole(authHeader, roles);
        return requireUserId(authHeader);
    }
}
