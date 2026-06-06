package com.yuntanfang.module.interaction.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.module.common.MockRecords;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InteractionController {

    @PostMapping("/api/follows/vendors/{vendorId}")
    public ApiResponse<Map<String, Object>> follow(@PathVariable Long vendorId) {
        return ApiResponse.ok(MockRecords.record(vendorId, "关注摊主", "followed"));
    }

    @DeleteMapping("/api/follows/vendors/{vendorId}")
    public ApiResponse<Map<String, Object>> unfollow(@PathVariable Long vendorId) {
        return ApiResponse.ok(MockRecords.record(vendorId, "取消关注", "deleted"));
    }

    @PostMapping("/api/favorites")
    public ApiResponse<Map<String, Object>> favorite(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "收藏", "created"));
    }

    @DeleteMapping("/api/favorites/{id}")
    public ApiResponse<Map<String, Object>> deleteFavorite(@PathVariable Long id) {
        return ApiResponse.ok(MockRecords.record(id, "收藏", "deleted"));
    }

    @PostMapping("/api/subscriptions")
    public ApiResponse<Map<String, Object>> subscribe(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "出摊提醒", "subscribed"));
    }
}
