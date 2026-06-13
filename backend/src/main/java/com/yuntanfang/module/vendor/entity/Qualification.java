package com.yuntanfang.module.vendor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_qualification")
public class Qualification {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vendorId;
    private String qualificationType;
    private String mediaUrl;
    private String status;
    private String auditOpinion;
    private String rejectReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
