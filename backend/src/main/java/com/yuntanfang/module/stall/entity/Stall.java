package com.yuntanfang.module.stall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_stall")
public class Stall {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vendorId;
    private Long areaId;
    private String stallName;
    private String businessStatus;
    private String category;
    private BigDecimal rating;
    private String address;
    private String distance;
    private String description;
    private String coverImage;
    private String vendorName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
