package com.yuntanfang.module.dashboard.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.complaint.entity.Complaint;
import com.yuntanfang.module.complaint.mapper.ComplaintMapper;
import com.yuntanfang.module.dashboard.entity.Policy;
import com.yuntanfang.module.dashboard.mapper.NoticeMapper;
import com.yuntanfang.module.dashboard.mapper.PolicyMapper;
import com.yuntanfang.module.message.service.MessageService;
import com.yuntanfang.module.order.entity.Order;
import com.yuntanfang.module.order.mapper.OrderMapper;
import com.yuntanfang.module.product.mapper.ProductMapper;
import com.yuntanfang.module.review.mapper.ReviewMapper;
import com.yuntanfang.module.stall.entity.Area;
import com.yuntanfang.module.stall.entity.Stall;
import com.yuntanfang.module.stall.mapper.AreaMapper;
import com.yuntanfang.module.stall.mapper.StallMapper;
import com.yuntanfang.module.user.mapper.UserMapper;
import com.yuntanfang.module.vendor.entity.Qualification;
import com.yuntanfang.module.vendor.entity.SpecialIdentity;
import com.yuntanfang.module.vendor.entity.StallReservation;
import com.yuntanfang.module.vendor.entity.Vendor;
import com.yuntanfang.module.vendor.mapper.QualificationMapper;
import com.yuntanfang.module.vendor.mapper.SpecialIdentityMapper;
import com.yuntanfang.module.vendor.mapper.StallReservationMapper;
import com.yuntanfang.module.vendor.mapper.VendorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final QualificationMapper qualificationMapper;
    private final SpecialIdentityMapper specialIdentityMapper;
    private final MessageService messageService;

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

    public PageResult<Vendor> vendorApplications() {
        return PageResult.of(vendorMapper.selectList(
                new LambdaQueryWrapper<Vendor>().orderByDesc(Vendor::getId)));
    }

    @Transactional
    public Vendor auditVendor(Long id, String status, String reason) {
        Vendor vendor = vendorMapper.selectById(id);
        if (vendor == null) {
            throw new BusinessException("摊主不存在");
        }
        String s = status == null ? "approved" : status;
        vendor.setStatus(s);
        if ("rejected".equals(s)) {
            vendor.setRejectReason(reason);
        } else {
            vendor.setRejectReason(null);
            vendor.setAuditOpinion(reason);
        }
        vendorMapper.updateById(vendor);
        messageService.create(vendor.getUserId(), "vendor", "user",
                "入驻审核" + ("approved".equals(s) ? "通过" : "未通过"),
                "approved".equals(s) ? "你的摊主入驻申请已通过。" : "你的摊主入驻申请被驳回：" + reason,
                "vendor_audit", vendor.getId());
        return vendor;
    }

    @Transactional
    public Qualification auditQualification(Long id, String status, String reason) {
        Qualification qualification = qualificationMapper.selectById(id);
        if (qualification == null) {
            throw new BusinessException("资质材料不存在");
        }
        String s = status == null ? "approved" : status;
        if ("approved".equals(s)) {
            requireVendorApproved(qualification.getVendorId(), "资质");
            qualification.setRejectReason(null);
            qualification.setAuditOpinion(reason);
        } else {
            qualification.setAuditOpinion(null);
            qualification.setRejectReason(reason);
        }
        qualification.setStatus(s);
        qualificationMapper.updateById(qualification);
        Vendor vendor = vendorMapper.selectById(qualification.getVendorId());
        if (vendor != null) {
            messageService.create(vendor.getUserId(), "vendor", "user",
                    "资质审核" + ("approved".equals(s) ? "通过" : "未通过"),
                    "approved".equals(s) ? "你的资质材料已通过。" : "你的资质材料被驳回：" + reason,
                    "qualification", qualification.getId());
        }
        return qualification;
    }

    @Transactional
    public SpecialIdentity auditSpecialIdentity(Long id, String status, String reason) {
        SpecialIdentity identity = specialIdentityMapper.selectById(id);
        if (identity == null) {
            throw new BusinessException("公益/特殊身份申请不存在");
        }
        String s = status == null ? "approved" : status;
        if ("approved".equals(s)) {
            requireVendorApproved(identity.getVendorId(), "公益认证");
            identity.setDisplayOnFront(1);
            identity.setAuditOpinion(reason);
            identity.setRejectReason(null);
        } else {
            identity.setDisplayOnFront(0);
            identity.setAuditOpinion(null);
            identity.setRejectReason(reason);
        }
        identity.setStatus(s);
        specialIdentityMapper.updateById(identity);
        Vendor vendor = vendorMapper.selectById(identity.getVendorId());
        if (vendor != null) {
            messageService.create(vendor.getUserId(), "vendor", "user",
                    "公益/特殊身份审核" + ("approved".equals(s) ? "通过" : "未通过"),
                    "approved".equals(s) ? "你的公益/特殊身份申请已通过。" : "你的公益/特殊身份申请被驳回：" + reason,
                    "special_identity", identity.getId());
        }
        return identity;
    }

    // 四级审核连贯：资质/公益/预约的通过都要求该摊主入驻审核已通过
    private void requireVendorApproved(Long vendorId, String scene) {
        Vendor vendor = vendorId == null ? null : vendorMapper.selectById(vendorId);
        if (vendor == null || !"approved".equals(vendor.getStatus())) {
            throw new BusinessException("请先通过该摊主的入驻审核，再审批" + scene);
        }
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
    public StallReservation auditReservation(Long id, String status, String reason) {
        StallReservation reservation = stallReservationMapper.selectById(id);
        if (reservation == null) {
            throw new BusinessException("摊位预约不存在");
        }
        String s = status == null ? "approved" : status;

        if ("approved".equals(s)) {
            if ("approved".equals(reservation.getStatus())) {
                throw new BusinessException("该预约已通过，无需重复审批");
            }
            Vendor vendor = vendorMapper.selectById(reservation.getVendorId());
            if (vendor == null || !"approved".equals(vendor.getStatus())) {
                throw new BusinessException("请先通过该摊主的入驻审核，再审批摊位预约");
            }
            requireVendorCertificationReady(vendor.getId());
            Stall stall = stallMapper.selectById(reservation.getStallId());
            if (stall == null) {
                throw new BusinessException("预约绑定的摊位不存在，无法释放");
            }
            if (stall.getVendorId() != null && !stall.getVendorId().equals(vendor.getId())) {
                throw new BusinessException("该摊位已绑定其他摊主，不能释放");
            }
            // 审批通过即"释放"摊位：绑定摊主并置为用户端可见
            stall.setVendorId(vendor.getId());
            stall.setVendorName(vendor.getVendorName());
            stall.setAuditStatus("approved");
            stall.setBusinessStatus("open");
            stallMapper.updateById(stall);
            // 同一摊位下其它仍待审批的预约自动驳回，避免重复占用
            StallReservation other = new StallReservation();
            other.setStatus("rejected");
            other.setRejectReason("摊位已分配给其他预约");
            stallReservationMapper.update(other, new LambdaQueryWrapper<StallReservation>()
                    .eq(StallReservation::getStallId, stall.getId())
                    .eq(StallReservation::getStatus, "pending")
                    .ne(StallReservation::getId, reservation.getId()));
            reservation.setStatus("approved");
            reservation.setRejectReason(null);
        } else {
            reservation.setStatus(s);
            reservation.setRejectReason(reason);
            // 若该摊位此前因本预约被释放，则回收（仅当当前绑定的就是本摊主）
            Stall stall = stallMapper.selectById(reservation.getStallId());
            if (stall != null && reservation.getVendorId() != null
                    && reservation.getVendorId().equals(stall.getVendorId())
                    && "approved".equals(stall.getAuditStatus())) {
                stall.setVendorId(null);
                stall.setVendorName(null);
                stall.setAuditStatus("pending");
                stall.setBusinessStatus("closed");
                stallMapper.updateById(stall);
            }
        }
        stallReservationMapper.updateById(reservation);
        Vendor vendor = vendorMapper.selectById(reservation.getVendorId());
        if (vendor != null) {
            messageService.create(vendor.getUserId(), "vendor", "user",
                    "摊位预约" + ("approved".equals(s) ? "通过" : "未通过"),
                    "approved".equals(s) ? "你的摊位预约已通过，摊位已释放。" : "你的摊位预约被驳回：" + reason,
                    "stall_reservation", reservation.getId());
        }
        return reservation;
    }

    private void requireVendorCertificationReady(Long vendorId) {
        long pendingQualifications = qualificationMapper.selectCount(new LambdaQueryWrapper<Qualification>()
                .eq(Qualification::getVendorId, vendorId)
                .eq(Qualification::getStatus, "pending"));
        if (pendingQualifications > 0) {
            throw new BusinessException("请先完成该摊主的资质审核，再审批摊位预约");
        }
        long rejectedQualifications = qualificationMapper.selectCount(new LambdaQueryWrapper<Qualification>()
                .eq(Qualification::getVendorId, vendorId)
                .eq(Qualification::getStatus, "rejected"));
        if (rejectedQualifications > 0) {
            throw new BusinessException("该摊主存在被驳回的资质材料，不能释放摊位");
        }
        long approvedQualifications = qualificationMapper.selectCount(new LambdaQueryWrapper<Qualification>()
                .eq(Qualification::getVendorId, vendorId)
                .eq(Qualification::getStatus, "approved"));
        if (approvedQualifications == 0) {
            throw new BusinessException("该摊主还没有已通过的资质材料，不能释放摊位");
        }

        long pendingIdentities = specialIdentityMapper.selectCount(new LambdaQueryWrapper<SpecialIdentity>()
                .eq(SpecialIdentity::getVendorId, vendorId)
                .eq(SpecialIdentity::getStatus, "pending"));
        if (pendingIdentities > 0) {
            throw new BusinessException("请先完成该摊主的公益/特殊身份审核，再审批摊位预约");
        }
        long rejectedIdentities = specialIdentityMapper.selectCount(new LambdaQueryWrapper<SpecialIdentity>()
                .eq(SpecialIdentity::getVendorId, vendorId)
                .eq(SpecialIdentity::getStatus, "rejected"));
        if (rejectedIdentities > 0) {
            throw new BusinessException("该摊主存在被驳回的公益/特殊身份申请，不能释放摊位");
        }
        long approvedIdentities = specialIdentityMapper.selectCount(new LambdaQueryWrapper<SpecialIdentity>()
                .eq(SpecialIdentity::getVendorId, vendorId)
                .eq(SpecialIdentity::getStatus, "approved"));
        if (approvedIdentities == 0) {
            throw new BusinessException("该摊主还没有已通过的公益/特殊身份申请，不能释放摊位");
        }
    }

    // ===== 审核队列（带摊主名称，供后台列表展示）=====

    private Map<Long, String> vendorNames() {
        return vendorMapper.selectList(null).stream()
                .collect(Collectors.toMap(Vendor::getId, Vendor::getVendorName, (a, b) -> a));
    }

    public PageResult<Map<String, Object>> qualificationQueue() {
        Map<Long, String> names = vendorNames();
        List<Map<String, Object>> rows = qualificationMapper.selectList(
                        new LambdaQueryWrapper<Qualification>().orderByDesc(Qualification::getId)).stream()
                .map(q -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", q.getId());
                    row.put("vendorId", q.getVendorId());
                    row.put("vendorName", names.get(q.getVendorId()));
                    row.put("qualificationType", q.getQualificationType());
                    row.put("mediaUrl", q.getMediaUrl());
                    row.put("status", q.getStatus());
                    row.put("auditOpinion", q.getAuditOpinion());
                    row.put("rejectReason", q.getRejectReason());
                    row.put("updatedAt", q.getUpdatedAt());
                    return row;
                })
                .collect(Collectors.toList());
        return PageResult.of(rows);
    }

    public PageResult<Map<String, Object>> specialIdentityQueue() {
        Map<Long, String> names = vendorNames();
        List<Map<String, Object>> rows = specialIdentityMapper.selectList(
                        new LambdaQueryWrapper<SpecialIdentity>().orderByDesc(SpecialIdentity::getId)).stream()
                .map(si -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", si.getId());
                    row.put("vendorId", si.getVendorId());
                    row.put("vendorName", names.get(si.getVendorId()));
                    row.put("identityType", si.getIdentityType());
                    row.put("publicWelfareTagId", si.getPublicWelfareTagId());
                    row.put("status", si.getStatus());
                    row.put("displayOnFront", si.getDisplayOnFront());
                    row.put("auditOpinion", si.getAuditOpinion());
                    row.put("rejectReason", si.getRejectReason());
                    row.put("updatedAt", si.getUpdatedAt());
                    return row;
                })
                .collect(Collectors.toList());
        return PageResult.of(rows);
    }

    public PageResult<Map<String, Object>> reservationQueue() {
        Map<Long, String> names = vendorNames();
        Map<Long, String> stallNames = stallMapper.selectList(null).stream()
                .collect(Collectors.toMap(Stall::getId, Stall::getStallName, (a, b) -> a));
        List<Map<String, Object>> rows = stallReservationMapper.selectList(
                        new LambdaQueryWrapper<StallReservation>().orderByDesc(StallReservation::getId)).stream()
                .map(r -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", r.getId());
                    row.put("vendorId", r.getVendorId());
                    row.put("vendorName", names.get(r.getVendorId()));
                    row.put("stallId", r.getStallId());
                    row.put("stallName", stallNames.get(r.getStallId()));
                    row.put("status", r.getStatus());
                    row.put("rejectReason", r.getRejectReason());
                    row.put("updatedAt", r.getUpdatedAt());
                    return row;
                })
                .collect(Collectors.toList());
        return PageResult.of(rows);
    }

    @Transactional
    public Complaint assignComplaint(Long id) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new BusinessException("投诉不存在");
        }
        complaint.setStatus("assigned");
        complaint.setAssigneeId(1L);
        complaintMapper.updateById(complaint);
        messageService.create(complaint.getUserId(), "consumer", "user", "投诉已受理",
                "你的投诉已进入处理流程。", "complaint", complaint.getId());
        return complaint;
    }

    public PageResult<Complaint> complaints() {
        return PageResult.of(complaintMapper.selectList(
                new LambdaQueryWrapper<Complaint>().orderByDesc(Complaint::getId)));
    }

    @Transactional
    public Complaint processComplaint(Long id, String status, String result) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new BusinessException("投诉不存在");
        }
        complaint.setStatus(status == null ? "processed" : status);
        complaint.setProcessResult(result == null || result.isBlank() ? "已记录并完成处理。" : result);
        complaint.setProcessorId(1L);
        complaintMapper.updateById(complaint);
        messageService.create(complaint.getUserId(), "consumer", "user", "投诉处理结果",
                complaint.getProcessResult(), "complaint", complaint.getId());
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
