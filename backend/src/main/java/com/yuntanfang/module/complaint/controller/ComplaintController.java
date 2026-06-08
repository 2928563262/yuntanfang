package com.yuntanfang.module.complaint.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.complaint.entity.Complaint;
import com.yuntanfang.module.complaint.service.ComplaintService;
import com.yuntanfang.security.AuthSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;
    private final AuthSupport authSupport;

    @PostMapping
    public ApiResponse<Complaint> create(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = authSupport.currentUserId(authorization);
        Long vendorId = body.get("vendorId") == null ? null : Long.valueOf(String.valueOf(body.get("vendorId")));
        String description = body.get("description") == null ? null : String.valueOf(body.get("description"));
        return ApiResponse.ok(complaintService.create(userId, vendorId, description));
    }

    @GetMapping("/my")
    public ApiResponse<PageResult<Complaint>> my(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(complaintService.my(authSupport.currentUserId(authorization)));
    }

    @GetMapping("/{id}")
    public ApiResponse<Complaint> detail(@PathVariable Long id) {
        return ApiResponse.ok(complaintService.detail(id));
    }
}
