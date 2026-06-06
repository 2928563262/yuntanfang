package com.yuntanfang.module.vendor.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.common.MockRecords;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    @PostMapping("/applications")
    public ApiResponse<Map<String, Object>> apply(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "入驻申请", "pending"));
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me() {
        return ApiResponse.ok(MockRecords.record(1L, "示例摊主", "active"));
    }

    @PutMapping("/me")
    public ApiResponse<Map<String, Object>> updateMe(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(body);
    }

    @PostMapping("/qualifications")
    public ApiResponse<Map<String, Object>> qualification(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "资质材料", "pending"));
    }

    @PostMapping("/special-identities")
    public ApiResponse<Map<String, Object>> specialIdentity(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "公益标签", "pending"));
    }

    @GetMapping("/stall-reservations")
    public ApiResponse<PageResult<Map<String, Object>>> reservations() {
        return ApiResponse.ok(PageResult.of(MockRecords.list("摊位预约")));
    }

    @PostMapping("/stall-reservations")
    public ApiResponse<Map<String, Object>> reserve(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "摊位预约", "pending"));
    }

    @PostMapping("/schedules")
    public ApiResponse<Map<String, Object>> schedule(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "出摊计划", "published"));
    }

    @PostMapping("/checkins")
    public ApiResponse<Map<String, Object>> checkin(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "经营打卡", "checked"));
    }

    @PostMapping("/products")
    public ApiResponse<Map<String, Object>> product(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "商品", "draft"));
    }

    @GetMapping("/orders")
    public ApiResponse<PageResult<Map<String, Object>>> orders() {
        return ApiResponse.ok(PageResult.of(MockRecords.list("商家订单")));
    }

    @PutMapping("/orders/{id}/status")
    public ApiResponse<Map<String, Object>> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "订单状态", String.valueOf(body.getOrDefault("status", "updated"))));
    }

    @PostMapping("/reviews/{id}/reply")
    public ApiResponse<Map<String, Object>> replyReview(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "评价回复", "replied"));
    }
}
