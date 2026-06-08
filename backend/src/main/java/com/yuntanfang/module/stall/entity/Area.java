package com.yuntanfang.module.stall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_area")
public class Area {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String areaName;
    private String fenceGeojson;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
