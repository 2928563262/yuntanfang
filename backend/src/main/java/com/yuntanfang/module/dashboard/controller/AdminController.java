package com.yuntanfang.module.dashboard.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.complaint.entity.Complaint;
import com.yuntanfang.module.dashboard.entity.Policy;
import com.yuntanfang.module.dashboard.service.DashboardService;
import com.yuntanfang.module.stall.entity.Area;
import com.yuntanfang.module.vendor.entity.StallReservation;
import com.yuntanfang.module.vendor.entity.Vendor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;

    private static String str(Map<String, Object> body, String key) {
        Object value = body.get(key);
        return value == null ? null : String.valueOf(value);
    }

    @GetMapping("/dashboard/overview")
    public ApiResponse<Map<String, Object>> overview() {
        return ApiResponse.ok(dashboardService.overview());
    }

    @GetMapping("/vendors/applications")
    public ApiResponse<PageResult<Vendor>> vendorApplications() {
        return ApiResponse.ok(dashboardService.vendorApplications());
    }

    @PutMapping("/vendors/applications/{id}/audit")
    public ApiResponse<Vendor> auditVendor(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.auditVendor(id, str(body, "status")));
    }

    @GetMapping("/{module}")
    public ApiResponse<PageResult<?>> list(@PathVariable String module) {
        return ApiResponse.ok(dashboardService.list(module));
    }

    @PutMapping("/{module}/{id}/audit")
    public ApiResponse<Vendor> audit(@PathVariable String module, @PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.auditVendor(id, str(body, "status")));
    }

    @PostMapping("/areas")
    public ApiResponse<Area> createArea(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.createArea(str(body, "areaName")));
    }

    @PutMapping("/areas/{id}")
    public ApiResponse<Area> updateArea(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.updateArea(id, str(body, "areaName"), str(body, "status")));
    }

    @PutMapping("/stall-reservations/{id}/audit")
    public ApiResponse<StallReservation> auditReservation(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.auditReservation(id, str(body, "status")));
    }

    @PutMapping("/complaints/{id}/assign")
    public ApiResponse<Complaint> assignComplaint(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.assignComplaint(id));
    }

    @PutMapping("/complaints/{id}/process")
    public ApiResponse<Complaint> processComplaint(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.processComplaint(id, str(body, "status")));
    }

    @PostMapping("/policies")
    public ApiResponse<Policy> createPolicy(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.createPolicy(str(body, "title"), str(body, "content")));
    }

    @PutMapping("/policies/{id}")
    public ApiResponse<Policy> updatePolicy(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return ApiResponse.ok(dashboardService.updatePolicy(id, str(body, "title"), str(body, "content")));
    }

    @PostMapping("/policies/{id}/publish")
    public ApiResponse<Policy> publishPolicy(@PathVariable Long id) {
        return ApiResponse.ok(dashboardService.publishPolicy(id));
    }
}
