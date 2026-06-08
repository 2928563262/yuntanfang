package com.yuntanfang.module.review.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_review")
public class Review {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long userId;
    private Integer rating;
    private String status;
    private String content;
    private String userName;
    private Long stallId;
    private String stallName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
