package com.yuntanfang.module.user.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.security.JwtTokenProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(jwtTokenProvider.loginPayload(request.username(), "consumer"));
    }

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(Map.of("userId", 1L, "username", request.username(), "role", "consumer"));
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me() {
        return ApiResponse.ok(Map.of("id", 1L, "username", "demo", "roles", new String[]{"consumer"}));
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }
}
