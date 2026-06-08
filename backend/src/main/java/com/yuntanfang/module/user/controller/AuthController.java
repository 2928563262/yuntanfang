package com.yuntanfang.module.user.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.module.user.service.UserAuthService;
import com.yuntanfang.security.AuthSupport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthService userAuthService;
    private final AuthSupport authSupport;

    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(userAuthService.login(request.username(), request.password()));
    }

    @PostMapping("/register")
    public ApiResponse<Map<String, Object>> register(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(userAuthService.register(request.username(), request.password()));
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(userAuthService.me(authSupport.currentUserId(authorization)));
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }
}
