package com.yuntanfang.module.complaint.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_complaint")
public class Complaint {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long vendorId;
    private Long orderId;
    private String complaintType;
    private String description;
    private String processResult;
    private Long assigneeId;
    private Long processorId;
    private String vendorName;
    private String targetName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
