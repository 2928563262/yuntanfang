package com.yuntanfang.module.interaction.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_subscribe")
public class Subscribe {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long vendorId;
    private String vendorName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
