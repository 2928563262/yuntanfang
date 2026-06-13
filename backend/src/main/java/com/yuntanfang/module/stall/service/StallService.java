package com.yuntanfang.module.stall.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.product.entity.Product;
import com.yuntanfang.module.product.mapper.ProductMapper;
import com.yuntanfang.module.review.entity.Review;
import com.yuntanfang.module.review.mapper.ReviewMapper;
import com.yuntanfang.module.stall.entity.Stall;
import com.yuntanfang.module.stall.mapper.StallMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StallService {

    private final StallMapper stallMapper;
    private final ProductMapper productMapper;
    private final ReviewMapper reviewMapper;

    // 用户端仅可见审核通过(audit_status=approved)的摊位
    private static final String VISIBLE = "approved";

    public PageResult<Stall> nearby(String category, long pageNo, long pageSize) {
        Page<Stall> page = stallMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapper<Stall>()
                        .eq(Stall::getAuditStatus, VISIBLE)
                        .eq(StringUtils.hasText(category), Stall::getCategory, category)
                        .orderByDesc(Stall::getId));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    public PageResult<Stall> search(String keyword, String category, long pageNo, long pageSize) {
        Page<Stall> page = stallMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapper<Stall>()
                        .eq(Stall::getAuditStatus, VISIBLE)
                        .like(StringUtils.hasText(keyword), Stall::getStallName, keyword)
                        .eq(StringUtils.hasText(category), Stall::getCategory, category)
                        .orderByDesc(Stall::getId));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    // 用户端只能访问审核通过的摊位；pending/rejected 一律按"不存在"处理，口径与 nearby/search 一致
    private Stall requireVisible(Long id) {
        Stall stall = stallMapper.selectById(id);
        if (stall == null || !VISIBLE.equals(stall.getAuditStatus())) {
            throw new BusinessException("摊位不存在");
        }
        return stall;
    }

    public Stall detail(Long id) {
        return requireVisible(id);
    }

    public PageResult<Product> products(Long stallId) {
        requireVisible(stallId);
        List<Product> list = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getStallId, stallId)
                .orderByDesc(Product::getId));
        return PageResult.of(list);
    }

    public PageResult<Review> reviews(Long stallId) {
        requireVisible(stallId);
        List<Review> list = reviewMapper.selectList(new LambdaQueryWrapper<Review>()
                .eq(Review::getStallId, stallId).orderByDesc(Review::getId));
        return PageResult.of(list);
    }
}
