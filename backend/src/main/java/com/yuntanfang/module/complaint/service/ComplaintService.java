package com.yuntanfang.module.complaint.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.complaint.entity.Complaint;
import com.yuntanfang.module.complaint.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintMapper complaintMapper;

    @Transactional
    public Complaint create(Long userId, Long vendorId, String description) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        Complaint complaint = new Complaint();
        complaint.setUserId(userId);
        complaint.setVendorId(vendorId);
        complaint.setDescription(description);
        complaint.setStatus("submitted");
        complaintMapper.insert(complaint);
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

    public Complaint detail(Long id) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new BusinessException("投诉不存在");
        }
        return complaint;
    }
}
