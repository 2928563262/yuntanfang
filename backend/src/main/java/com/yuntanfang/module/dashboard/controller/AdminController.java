package com.yuntanfang.module.dashboard.controller;

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
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard/overview")
    public ApiResponse<Map<String, Object>> overview() {
        return ApiResponse.ok(Map.of("vendors", 12, "users", 120, "orders", 36, "complaints", 3));
    }

    @GetMapping("/{module}")
    public ApiResponse<PageResult<Map<String, Object>>> list(@PathVariable String module) {
        return ApiResponse.ok(PageResult.of(MockRecords.list(module)));
    }

    @GetMapping("/vendors/applications")
    public ApiResponse<PageResult<Map<String, Object>>> vendorApplications() {
        return ApiResponse.ok(PageResult.of(MockRecords.list("入驻审核")));
    }

    @PutMapping("/vendors/applications/{id}/audit")
    public ApiResponse<Map<String, Object>> auditVendor(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "入驻审核", String.valueOf(body.getOrDefault("status", "approved"))));
    }

    @PutMapping("/{module}/{id}/audit")
    public ApiResponse<Map<String, Object>> audit(@PathVariable String module, @PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, module + "审核", String.valueOf(body.getOrDefault("status", "approved"))));
    }

    @PostMapping("/areas")
    public ApiResponse<Map<String, Object>> createArea(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "经营区域", "enabled"));
    }

    @PutMapping("/areas/{id}")
    public ApiResponse<Map<String, Object>> updateArea(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "经营区域", "updated"));
    }

    @PutMapping("/stall-reservations/{id}/audit")
    public ApiResponse<Map<String, Object>> auditReservation(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "摊位预约审批", String.valueOf(body.getOrDefault("status", "approved"))));
    }

    @PutMapping("/complaints/{id}/assign")
    public ApiResponse<Map<String, Object>> assignComplaint(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "投诉分派", "assigned"));
    }

    @PutMapping("/complaints/{id}/process")
    public ApiResponse<Map<String, Object>> processComplaint(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "投诉处理", "processed"));
    }

    @PostMapping("/policies")
    public ApiResponse<Map<String, Object>> createPolicy(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "政策公告", "draft"));
    }

    @PutMapping("/policies/{id}")
    public ApiResponse<Map<String, Object>> updatePolicy(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(id, "政策公告", "updated"));
    }

    @PostMapping("/policies/{id}/publish")
    public ApiResponse<Map<String, Object>> publishPolicy(@PathVariable Long id) {
        return ApiResponse.ok(MockRecords.record(id, "政策公告", "published"));
    }
}
