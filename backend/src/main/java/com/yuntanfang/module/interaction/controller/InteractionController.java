package com.yuntanfang.module.interaction.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.interaction.entity.Favorite;
import com.yuntanfang.module.interaction.entity.Follow;
import com.yuntanfang.module.interaction.entity.Subscribe;
import com.yuntanfang.module.interaction.service.InteractionService;
import com.yuntanfang.security.AuthSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class InteractionController {

    private final InteractionService interactionService;
    private final AuthSupport authSupport;

    private Long uid(String authorization) {
        return authSupport.currentUserId(authorization);
    }

    private static String str(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private static Long lng(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : Long.valueOf(String.valueOf(value));
    }

    @PostMapping("/api/follows/vendors/{vendorId}")
    public ApiResponse<Follow> follow(@PathVariable Long vendorId,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(interactionService.follow(uid(authorization), vendorId));
    }

    @DeleteMapping("/api/follows/vendors/{vendorId}")
    public ApiResponse<Map<String, Object>> unfollow(@PathVariable Long vendorId,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        interactionService.unfollow(uid(authorization), vendorId);
        return ApiResponse.ok(Map.of("vendorId", vendorId, "status", "deleted"));
    }

    @GetMapping("/api/follows")
    public ApiResponse<PageResult<Follow>> follows(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(interactionService.follows(uid(authorization)));
    }

    @PostMapping("/api/favorites")
    public ApiResponse<Favorite> favorite(@RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(interactionService.favorite(uid(authorization), str(body, "bizType"), lng(body, "bizId"), str(body, "bizName")));
    }

    @DeleteMapping("/api/favorites/{id}")
    public ApiResponse<Map<String, Object>> deleteFavorite(@PathVariable Long id) {
        interactionService.deleteFavorite(id);
        return ApiResponse.ok(Map.of("id", id, "status", "deleted"));
    }

    @GetMapping("/api/favorites")
    public ApiResponse<PageResult<Favorite>> favorites(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(interactionService.favorites(uid(authorization)));
    }

    @PostMapping("/api/subscriptions")
    public ApiResponse<Subscribe> subscribe(@RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(interactionService.subscribe(uid(authorization), lng(body, "vendorId"), str(body, "vendorName")));
    }

    @GetMapping("/api/subscriptions")
    public ApiResponse<PageResult<Subscribe>> subscriptions(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(interactionService.subscriptions(uid(authorization)));
    }
}
