package com.yuntanfang.module.stall.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.product.entity.Product;
import com.yuntanfang.module.review.entity.Review;
import com.yuntanfang.module.stall.entity.Stall;
import com.yuntanfang.module.stall.service.StallService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stalls")
@RequiredArgsConstructor
public class StallController {

    private final StallService stallService;

    @GetMapping("/nearby")
    public ApiResponse<PageResult<Stall>> nearby(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "20") long pageSize) {
        return ApiResponse.ok(stallService.nearby(category, pageNo, pageSize));
    }

    @GetMapping("/search")
    public ApiResponse<PageResult<Stall>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") long pageNo,
            @RequestParam(defaultValue = "20") long pageSize) {
        return ApiResponse.ok(stallService.search(keyword, category, pageNo, pageSize));
    }

    @GetMapping("/{id}")
    public ApiResponse<Stall> detail(@PathVariable Long id) {
        return ApiResponse.ok(stallService.detail(id));
    }

    @GetMapping("/{id}/products")
    public ApiResponse<PageResult<Product>> products(@PathVariable Long id) {
        return ApiResponse.ok(stallService.products(id));
    }

    @GetMapping("/{id}/reviews")
    public ApiResponse<PageResult<Review>> reviews(@PathVariable Long id) {
        return ApiResponse.ok(stallService.reviews(id));
    }
}
