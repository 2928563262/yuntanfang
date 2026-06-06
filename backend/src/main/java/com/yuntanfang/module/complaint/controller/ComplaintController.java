package com.yuntanfang.module.complaint.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.common.MockRecords;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        return ApiResponse.ok(MockRecords.record(1L, "投诉工单", "submitted"));
    }

    @GetMapping("/my")
    public ApiResponse<PageResult<Map<String, Object>>> my() {
        return ApiResponse.ok(PageResult.of(MockRecords.list("我的投诉")));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        return ApiResponse.ok(MockRecords.record(id, "投诉详情", "processing"));
    }
}
