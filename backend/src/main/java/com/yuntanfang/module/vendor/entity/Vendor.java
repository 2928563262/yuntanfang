package com.yuntanfang.module.vendor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_vendor")
public class Vendor {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String vendorName;
    private String story;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
