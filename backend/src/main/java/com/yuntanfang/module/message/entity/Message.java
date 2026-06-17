package com.yuntanfang.module.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_message")
public class Message {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String role;
    private String targetType;
    private String title;
    private String content;
    private String bizType;
    private Long bizId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
