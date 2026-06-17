package com.yuntanfang.module.complaint.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.complaint.entity.Complaint;
import com.yuntanfang.module.complaint.mapper.ComplaintMapper;
import com.yuntanfang.module.message.service.MessageService;
import com.yuntanfang.module.order.entity.Order;
import com.yuntanfang.module.order.mapper.OrderMapper;
import com.yuntanfang.module.vendor.entity.Vendor;
import com.yuntanfang.module.vendor.mapper.VendorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintMapper complaintMapper;
    private final OrderMapper orderMapper;
    private final VendorMapper vendorMapper;
    private final MessageService messageService;

    @Transactional
    public Complaint create(Long userId, Long vendorId, Long orderId, String type, String description) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        Order order = orderId == null ? null : orderMapper.selectById(orderId);
        if (order != null) {
            if (!userId.equals(order.getUserId())) {
                throw new BusinessException("只能投诉自己的订单");
            }
            vendorId = order.getVendorId();
        }
        Vendor vendor = vendorId == null ? null : vendorMapper.selectById(vendorId);
        Complaint complaint = new Complaint();
        complaint.setUserId(userId);
        complaint.setVendorId(vendorId);
        complaint.setOrderId(orderId);
        complaint.setComplaintType(type == null || type.isBlank() ? "service" : type);
        complaint.setDescription(description);
        complaint.setVendorName(vendor == null ? null : vendor.getVendorName());
        complaint.setTargetName(order == null ? (vendor == null ? "平台投诉" : vendor.getVendorName()) : order.getStallName());
        complaint.setStatus("submitted");
        complaintMapper.insert(complaint);
        messageService.create(userId, "consumer", "user", "投诉已提交",
                "投诉工单已提交，平台会尽快处理。", "complaint", complaint.getId());
        messageService.create(null, "admin", "role", "新投诉工单",
                "用户提交了新的投诉，请及时处理。", "complaint", complaint.getId());
        return complaint;
    }

    public PageResult<Complaint> my(Long userId) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        List<Complaint> list = complaintMapper.selectList(new LambdaQueryWrapper<Complaint>()
                .eq(Complaint::getUserId, userId).orderByDesc(Complaint::getId));
        return PageResult.of(list);
    }

    public Complaint detail(Long id, Long userId) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null || !complaint.getUserId().equals(userId)) {
            throw new BusinessException("投诉不存在");
        }
        return complaint;
    }
}
