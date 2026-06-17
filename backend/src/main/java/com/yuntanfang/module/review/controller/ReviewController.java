package com.yuntanfang.module.review.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.review.entity.Review;
import com.yuntanfang.module.review.service.ReviewService;
import com.yuntanfang.security.AuthSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthSupport authSupport;

    @GetMapping("/my")
    public ApiResponse<PageResult<Review>> my(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(reviewService.my(authSupport.requireUserIdWithRole(authorization, "consumer")));
    }
}
