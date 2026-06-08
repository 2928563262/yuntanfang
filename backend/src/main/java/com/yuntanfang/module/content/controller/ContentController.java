package com.yuntanfang.module.content.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.dashboard.entity.Notice;
import com.yuntanfang.module.dashboard.entity.Policy;
import com.yuntanfang.module.dashboard.mapper.NoticeMapper;
import com.yuntanfang.module.dashboard.mapper.PolicyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContentController {

    private final PolicyMapper policyMapper;
    private final NoticeMapper noticeMapper;

    @GetMapping("/policies")
    public ApiResponse<PageResult<Policy>> policies() {
        return ApiResponse.ok(PageResult.of(policyMapper.selectList(
                new LambdaQueryWrapper<Policy>().orderByDesc(Policy::getId))));
    }

    @GetMapping("/notices")
    public ApiResponse<PageResult<Notice>> notices() {
        return ApiResponse.ok(PageResult.of(noticeMapper.selectList(
                new LambdaQueryWrapper<Notice>().orderByDesc(Notice::getId))));
    }
}
