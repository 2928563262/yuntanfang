package com.yuntanfang.module.vendor.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.order.entity.Order;
import com.yuntanfang.module.order.entity.OrderStatusLog;
import com.yuntanfang.module.order.mapper.OrderMapper;
import com.yuntanfang.module.order.mapper.OrderStatusLogMapper;
import com.yuntanfang.module.product.entity.Product;
import com.yuntanfang.module.product.mapper.ProductMapper;
import com.yuntanfang.module.vendor.entity.Qualification;
import com.yuntanfang.module.vendor.entity.SpecialIdentity;
import com.yuntanfang.module.vendor.entity.StallCheckin;
import com.yuntanfang.module.vendor.entity.StallReservation;
import com.yuntanfang.module.vendor.entity.StallSchedule;
import com.yuntanfang.module.vendor.entity.Vendor;
import com.yuntanfang.module.vendor.mapper.QualificationMapper;
import com.yuntanfang.module.vendor.mapper.SpecialIdentityMapper;
import com.yuntanfang.module.vendor.mapper.StallCheckinMapper;
import com.yuntanfang.module.vendor.mapper.StallReservationMapper;
import com.yuntanfang.module.vendor.mapper.StallScheduleMapper;
import com.yuntanfang.module.vendor.mapper.VendorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorMapper vendorMapper;
    private final QualificationMapper qualificationMapper;
    private final SpecialIdentityMapper specialIdentityMapper;
    private final StallReservationMapper stallReservationMapper;
    private final StallScheduleMapper stallScheduleMapper;
    private final StallCheckinMapper stallCheckinMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderStatusLogMapper orderStatusLogMapper;

    private Vendor requireVendor(Long userId) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        Vendor vendor = vendorMapper.selectOne(new LambdaQueryWrapper<Vendor>().eq(Vendor::getUserId, userId));
        if (vendor == null) {
            throw new BusinessException("当前账号不是摊主或尚未入驻");
        }
        return vendor;
    }

    public Vendor me(Long userId) {
        return requireVendor(userId);
    }

    @Transactional
    public Vendor apply(Long userId, String vendorName, String story) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        Vendor existing = vendorMapper.selectOne(new LambdaQueryWrapper<Vendor>().eq(Vendor::getUserId, userId));
        if (existing != null) {
            throw new BusinessException("已提交过入驻申请");
        }
        Vendor vendor = new Vendor();
        vendor.setUserId(userId);
        vendor.setVendorName(vendorName == null ? "新摊主" : vendorName);
        vendor.setStory(story);
        vendor.setStatus("pending");
        vendorMapper.insert(vendor);
        return vendor;
    }

    @Transactional
    public Vendor updateMe(Long userId, String vendorName, String story) {
        Vendor vendor = requireVendor(userId);
        if (vendorName != null) {
            vendor.setVendorName(vendorName);
        }
        if (story != null) {
            vendor.setStory(story);
        }
        vendorMapper.updateById(vendor);
        return vendor;
    }

    @Transactional
    public Qualification addQualification(Long userId, String type, String mediaUrl) {
        Vendor vendor = requireVendor(userId);
        Qualification qualification = new Qualification();
        qualification.setVendorId(vendor.getId());
        qualification.setQualificationType(type == null ? "unknown" : type);
        qualification.setMediaUrl(mediaUrl);
        qualification.setStatus("pending");
        qualificationMapper.insert(qualification);
        return qualification;
    }

    @Transactional
    public SpecialIdentity addSpecialIdentity(Long userId, String identityType) {
        Vendor vendor = requireVendor(userId);
        SpecialIdentity identity = new SpecialIdentity();
        identity.setVendorId(vendor.getId());
        identity.setIdentityType(identityType == null ? "public_welfare" : identityType);
        identity.setStatus("pending");
        specialIdentityMapper.insert(identity);
        return identity;
    }

    public PageResult<StallReservation> reservations(Long userId) {
        Vendor vendor = requireVendor(userId);
        List<StallReservation> list = stallReservationMapper.selectList(new LambdaQueryWrapper<StallReservation>()
                .eq(StallReservation::getVendorId, vendor.getId()).orderByDesc(StallReservation::getId));
        return PageResult.of(list);
    }

    @Transactional
    public StallReservation reserve(Long userId, Long stallId) {
        Vendor vendor = requireVendor(userId);
        StallReservation reservation = new StallReservation();
        reservation.setVendorId(vendor.getId());
        reservation.setStallId(stallId);
        reservation.setStatus("pending");
        stallReservationMapper.insert(reservation);
        return reservation;
    }

    @Transactional
    public StallSchedule schedule(Long userId, Long stallId, String date) {
        requireVendor(userId);
        StallSchedule schedule = new StallSchedule();
        schedule.setStallId(stallId);
        if (date != null) {
            try {
                schedule.setScheduleDate(LocalDate.parse(date));
            } catch (Exception ignore) {
                // 忽略非法日期，保持为空
            }
        }
        schedule.setStatus("published");
        stallScheduleMapper.insert(schedule);
        return schedule;
    }

    @Transactional
    public StallCheckin checkin(Long userId, Long stallId, Object latitude, Object longitude) {
        requireVendor(userId);
        StallCheckin checkin = new StallCheckin();
        checkin.setStallId(stallId);
        if (latitude != null) {
            checkin.setLatitude(new BigDecimal(String.valueOf(latitude)));
        }
        if (longitude != null) {
            checkin.setLongitude(new BigDecimal(String.valueOf(longitude)));
        }
        checkin.setStatus("checked");
        stallCheckinMapper.insert(checkin);
        return checkin;
    }

    @Transactional
    public Product addProduct(Long userId, String name, Object price, Long categoryId) {
        Vendor vendor = requireVendor(userId);
        Product product = new Product();
        product.setVendorId(vendor.getId());
        product.setCategoryId(categoryId);
        product.setProductName(name == null ? "新商品" : name);
        if (price != null) {
            product.setPrice(new BigDecimal(String.valueOf(price)));
        }
        product.setStatus("draft");
        productMapper.insert(product);
        return product;
    }

    public PageResult<Order> orders(Long userId) {
        Vendor vendor = requireVendor(userId);
        List<Order> list = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getVendorId, vendor.getId()).orderByDesc(Order::getId));
        return PageResult.of(list);
    }

    public PageResult<Product> products(Long userId) {
        Vendor vendor = requireVendor(userId);
        List<Product> list = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getVendorId, vendor.getId()).orderByDesc(Product::getId));
        return PageResult.of(list);
    }

    @Transactional
    public Order updateOrderStatus(Long userId, Long orderId, String status) {
        Vendor vendor = requireVendor(userId);
        Order order = orderMapper.selectById(orderId);
        if (order == null || !vendor.getId().equals(order.getVendorId())) {
            throw new BusinessException("订单不存在或无权操作");
        }
        if (status != null) {
            order.setOrderStatus(status);
        }
        orderMapper.updateById(order);
        OrderStatusLog log = new OrderStatusLog();
        log.setOrderId(orderId);
        log.setOrderStatus(order.getOrderStatus());
        orderStatusLogMapper.insert(log);
        return order;
    }

    public Map<String, Object> replyReview(Long userId, Long reviewId, String reply) {
        requireVendor(userId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("reviewId", reviewId);
        result.put("reply", reply);
        result.put("status", "replied");
        return result;
    }
}
