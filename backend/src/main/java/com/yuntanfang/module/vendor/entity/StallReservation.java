package com.yuntanfang.module.vendor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_stall_reservation")
public class StallReservation {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long vendorId;
    private Long stallId;
    private String status;
    private String rejectReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
