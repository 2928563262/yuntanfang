package com.yuntanfang.module.dashboard.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.complaint.entity.Complaint;
import com.yuntanfang.module.complaint.mapper.ComplaintMapper;
import com.yuntanfang.module.dashboard.entity.Policy;
import com.yuntanfang.module.dashboard.mapper.NoticeMapper;
import com.yuntanfang.module.dashboard.mapper.PolicyMapper;
import com.yuntanfang.module.order.entity.Order;
import com.yuntanfang.module.order.mapper.OrderMapper;
import com.yuntanfang.module.product.mapper.ProductMapper;
import com.yuntanfang.module.review.mapper.ReviewMapper;
import com.yuntanfang.module.stall.entity.Area;
import com.yuntanfang.module.stall.mapper.AreaMapper;
import com.yuntanfang.module.stall.mapper.StallMapper;
import com.yuntanfang.module.user.mapper.UserMapper;
import com.yuntanfang.module.vendor.entity.StallReservation;
import com.yuntanfang.module.vendor.entity.Vendor;
import com.yuntanfang.module.vendor.mapper.StallReservationMapper;
import com.yuntanfang.module.vendor.mapper.VendorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VendorMapper vendorMapper;
    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final ComplaintMapper complaintMapper;
    private final AreaMapper areaMapper;
    private final StallMapper stallMapper;
    private final ProductMapper productMapper;
    private final ReviewMapper reviewMapper;
    private final PolicyMapper policyMapper;
    private final NoticeMapper noticeMapper;
    private final StallReservationMapper stallReservationMapper;

    public Map<String, Object> overview() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("vendors", vendorMapper.selectCount(null));
        result.put("users", userMapper.selectCount(null));
        result.put("orders", orderMapper.selectCount(null));
        result.put("complaints", complaintMapper.selectCount(null));
        result.put("stalls", stallMapper.selectCount(null));
        result.put("products", productMapper.selectCount(null));
        return result;
    }

    public PageResult<?> list(String module) {
        List<?> data = switch (module == null ? "" : module) {
            case "vendors", "vendor" -> vendorMapper.selectList(null);
            case "users", "user" -> userMapper.selectList(null);
            case "orders", "order" -> orderMapper.selectList(null);
            case "complaints", "complaint" -> complaintMapper.selectList(null);
            case "areas", "area" -> areaMapper.selectList(null);
            case "stalls", "stall" -> stallMapper.selectList(null);
            case "products", "product" -> productMapper.selectList(null);
            case "reviews", "review" -> reviewMapper.selectList(null);
            case "policies", "policy" -> policyMapper.selectList(null);
            case "notices", "notice" -> noticeMapper.selectList(null);
            case "stall-reservations", "reservations" -> stallReservationMapper.selectList(null);
            default -> List.of();
        };
        return PageResult.of(data);
    }

    public PageResult<Vendor> vendorApplications() {
        return PageResult.of(vendorMapper.selectList(
                new LambdaQueryWrapper<Vendor>().orderByDesc(Vendor::getId)));
    }

    @Transactional
    public Vendor auditVendor(Long id, String status) {
        Vendor vendor = vendorMapper.selectById(id);
        if (vendor == null) {
            throw new BusinessException("摊主不存在");
        }
        vendor.setStatus(status == null ? "approved" : status);
        vendorMapper.updateById(vendor);
        return vendor;
    }

    @Transactional
    public Area createArea(String name) {
        Area area = new Area();
        area.setAreaName(name == null ? "新经营区域" : name);
        area.setStatus("enabled");
        areaMapper.insert(area);
        return area;
    }

    @Transactional
    public Area updateArea(Long id, String name, String status) {
        Area area = areaMapper.selectById(id);
        if (area == null) {
            throw new BusinessException("经营区域不存在");
        }
        if (name != null) {
            area.setAreaName(name);
        }
        if (status != null) {
            area.setStatus(status);
        }
        areaMapper.updateById(area);
        return area;
    }

    @Transactional
    public StallReservation auditReservation(Long id, String status) {
        StallReservation reservation = stallReservationMapper.selectById(id);
        if (reservation == null) {
            throw new BusinessException("摊位预约不存在");
        }
        reservation.setStatus(status == null ? "approved" : status);
        stallReservationMapper.updateById(reservation);
        return reservation;
    }

    @Transactional
    public Complaint assignComplaint(Long id) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new BusinessException("投诉不存在");
        }
        complaint.setStatus("assigned");
        complaintMapper.updateById(complaint);
        return complaint;
    }

    @Transactional
    public Complaint processComplaint(Long id, String status) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new BusinessException("投诉不存在");
        }
        complaint.setStatus(status == null ? "processed" : status);
        complaintMapper.updateById(complaint);
        return complaint;
    }

    @Transactional
    public Policy createPolicy(String title, String content) {
        Policy policy = new Policy();
        policy.setTitle(title == null ? "新公告" : title);
        policy.setContent(content);
        policy.setStatus("draft");
        policyMapper.insert(policy);
        return policy;
    }

    @Transactional
    public Policy updatePolicy(Long id, String title, String content) {
        Policy policy = policyMapper.selectById(id);
        if (policy == null) {
            throw new BusinessException("公告不存在");
        }
        if (title != null) {
            policy.setTitle(title);
        }
        if (content != null) {
            policy.setContent(content);
        }
        policyMapper.updateById(policy);
        return policy;
    }

    @Transactional
    public Policy publishPolicy(Long id) {
        Policy policy = policyMapper.selectById(id);
        if (policy == null) {
            throw new BusinessException("公告不存在");
        }
        policy.setStatus("published");
        policyMapper.updateById(policy);
        return policy;
    }
}
