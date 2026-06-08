package com.yuntanfang.module.interaction.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.interaction.entity.Favorite;
import com.yuntanfang.module.interaction.entity.Follow;
import com.yuntanfang.module.interaction.entity.Subscribe;
import com.yuntanfang.module.interaction.mapper.FavoriteMapper;
import com.yuntanfang.module.interaction.mapper.FollowMapper;
import com.yuntanfang.module.interaction.mapper.SubscribeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private final FollowMapper followMapper;
    private final FavoriteMapper favoriteMapper;
    private final SubscribeMapper subscribeMapper;

    private static Long requireUser(Long userId) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        return userId;
    }

    @Transactional
    public Follow follow(Long userId, Long vendorId) {
        requireUser(userId);
        Follow follow = followMapper.selectOne(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getUserId, userId).eq(Follow::getVendorId, vendorId));
        if (follow == null) {
            follow = new Follow();
            follow.setUserId(userId);
            follow.setVendorId(vendorId);
            follow.setStatus("active");
            followMapper.insert(follow);
        } else {
            follow.setStatus("active");
            followMapper.updateById(follow);
        }
        return follow;
    }

    @Transactional
    public void unfollow(Long userId, Long vendorId) {
        requireUser(userId);
        followMapper.delete(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getUserId, userId).eq(Follow::getVendorId, vendorId));
    }

    public PageResult<Follow> follows(Long userId) {
        requireUser(userId);
        List<Follow> list = followMapper.selectList(new LambdaQueryWrapper<Follow>()
                .eq(Follow::getUserId, userId).eq(Follow::getStatus, "active").orderByDesc(Follow::getId));
        return PageResult.of(list);
    }

    @Transactional
    public Favorite favorite(Long userId, String bizType, Long bizId, String bizName) {
        requireUser(userId);
        Favorite favorite = favoriteMapper.selectOne(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId)
                .eq(Favorite::getBizType, bizType == null ? "stall" : bizType)
                .eq(Favorite::getBizId, bizId));
        if (favorite != null) {
            favorite.setStatus("active");
            favorite.setBizName(bizName);
            favoriteMapper.updateById(favorite);
            return favorite;
        }
        favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setBizType(bizType == null ? "stall" : bizType);
        favorite.setBizId(bizId);
        favorite.setBizName(bizName);
        favorite.setStatus("active");
        favoriteMapper.insert(favorite);
        return favorite;
    }

    @Transactional
    public void deleteFavorite(Long id) {
        favoriteMapper.deleteById(id);
    }

    public PageResult<Favorite> favorites(Long userId) {
        requireUser(userId);
        List<Favorite> list = favoriteMapper.selectList(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, userId).eq(Favorite::getStatus, "active").orderByDesc(Favorite::getId));
        return PageResult.of(list);
    }

    @Transactional
    public Subscribe subscribe(Long userId, Long vendorId, String vendorName) {
        requireUser(userId);
        Subscribe subscribe = subscribeMapper.selectOne(new LambdaQueryWrapper<Subscribe>()
                .eq(Subscribe::getUserId, userId).eq(Subscribe::getVendorId, vendorId));
        if (subscribe != null) {
            subscribe.setStatus("active");
            subscribe.setVendorName(vendorName);
            subscribeMapper.updateById(subscribe);
            return subscribe;
        }
        subscribe = new Subscribe();
        subscribe.setUserId(userId);
        subscribe.setVendorId(vendorId);
        subscribe.setVendorName(vendorName);
        subscribe.setStatus("active");
        subscribeMapper.insert(subscribe);
        return subscribe;
    }

    public PageResult<Subscribe> subscriptions(Long userId) {
        requireUser(userId);
        List<Subscribe> list = subscribeMapper.selectList(new LambdaQueryWrapper<Subscribe>()
                .eq(Subscribe::getUserId, userId).eq(Subscribe::getStatus, "active").orderByDesc(Subscribe::getId));
        return PageResult.of(list);
    }
}
