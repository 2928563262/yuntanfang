package com.yuntanfang.module.stall.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.common.MockRecords;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stalls")
public class StallController {

    @GetMapping("/nearby")
    public ApiResponse<PageResult<Map<String, Object>>> nearby() {
        return ApiResponse.ok(PageResult.of(MockRecords.list("附近摊位")));
    }

    @GetMapping("/search")
    public ApiResponse<PageResult<Map<String, Object>>> search(@RequestParam(required = false) String keyword) {
        return ApiResponse.ok(PageResult.of(MockRecords.list(keyword == null ? "摊位搜索" : keyword)));
    }

    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> detail(@PathVariable Long id) {
        return ApiResponse.ok(MockRecords.record(id, "烟火小摊", "open"));
    }

    @GetMapping("/{id}/products")
    public ApiResponse<PageResult<Map<String, Object>>> products(@PathVariable Long id) {
        return ApiResponse.ok(PageResult.of(MockRecords.list("摊位商品")));
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse<PageResult<Map<String, Object>>> reviews(@PathVariable Long id) {
        return ApiResponse.ok(PageResult.of(MockRecords.list("摊位评价")));
    }
}
