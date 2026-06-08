package com.yuntanfang.module.vendor.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("t_stall_schedule")
public class StallSchedule {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stallId;
    private LocalDate scheduleDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
