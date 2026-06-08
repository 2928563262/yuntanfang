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

    public PageResult<Stall> nearby(String category, long pageNo, long pageSize) {
        Page<Stall> page = stallMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapper<Stall>()
                        .eq(StringUtils.hasText(category), Stall::getCategory, category)
                        .orderByDesc(Stall::getId));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    public PageResult<Stall> search(String keyword, String category, long pageNo, long pageSize) {
        Page<Stall> page = stallMapper.selectPage(new Page<>(pageNo, pageSize),
                new LambdaQueryWrapper<Stall>()
                        .like(StringUtils.hasText(keyword), Stall::getStallName, keyword)
                        .eq(StringUtils.hasText(category), Stall::getCategory, category)
                        .orderByDesc(Stall::getId));
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    public Stall detail(Long id) {
        Stall stall = stallMapper.selectById(id);
        if (stall == null) {
            throw new BusinessException("摊位不存在");
        }
        return stall;
    }

    public PageResult<Product> products(Long stallId) {
        Stall stall = stallMapper.selectById(stallId);
        if (stall == null) {
            throw new BusinessException("摊位不存在");
        }
        List<Product> list = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(stall.getVendorId() != null, Product::getVendorId, stall.getVendorId())
                .orderByDesc(Product::getId));
        return PageResult.of(list);
    }

    public PageResult<Review> reviews(Long stallId) {
        List<Review> list = reviewMapper.selectList(new LambdaQueryWrapper<Review>()
                .eq(Review::getStallId, stallId).orderByDesc(Review::getId));
        return PageResult.of(list);
    }
}
