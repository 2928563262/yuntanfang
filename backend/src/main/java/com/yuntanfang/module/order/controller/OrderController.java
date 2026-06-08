package com.yuntanfang.module.order.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.order.entity.Order;
import com.yuntanfang.module.order.service.OrderService;
import com.yuntanfang.module.review.entity.Review;
import com.yuntanfang.security.AuthSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthSupport authSupport;

    private static String str(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : String.valueOf(value);
    }

    private static Long lng(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : Long.valueOf(String.valueOf(value));
    }

    @PostMapping
    @SuppressWarnings("unchecked")
    public ApiResponse<Order> create(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = authSupport.currentUserId(authorization);
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        return ApiResponse.ok(orderService.create(
                userId,
                lng(body, "vendorId"),
                lng(body, "stallId"),
                str(body, "stallName"),
                str(body, "pickupTime"),
                str(body, "contactPhone"),
                str(body, "remark"),
                items));
    }

    @GetMapping("/my")
    public ApiResponse<PageResult<Map<String, Object>>> my(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "10") long pageSize) {
        return ApiResponse.ok(orderService.myWithItems(authSupport.currentUserId(authorization), pageNo, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        return ApiResponse.ok(orderService.detail(id));
    }

    @PostMapping("/{id}/reviews")
    public ApiResponse<Review> review(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = authSupport.currentUserId(authorization);
        Integer rating = body.get("rating") == null ? null : Integer.valueOf(String.valueOf(body.get("rating")));
        return ApiResponse.ok(orderService.review(id, userId, rating));
    }
}
