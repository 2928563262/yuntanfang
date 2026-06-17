package com.yuntanfang.module.vendor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_special_identity")
public class SpecialIdentity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vendorId;
    private String identityType;
    private String status;
    private Long publicWelfareTagId;
    private Long auditorId;
    private String auditOpinion;
    private String rejectReason;
    private Integer displayOnFront;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
