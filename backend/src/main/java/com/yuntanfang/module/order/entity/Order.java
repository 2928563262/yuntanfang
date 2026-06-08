package com.yuntanfang.module.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_order")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long vendorId;
    private Long stallId;
    private String stallName;
    private String orderStatus;
    private BigDecimal totalAmount;
    private String pickupTime;
    private String contactPhone;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
