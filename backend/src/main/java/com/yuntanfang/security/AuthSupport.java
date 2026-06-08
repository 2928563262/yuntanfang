package com.yuntanfang.security;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
}
