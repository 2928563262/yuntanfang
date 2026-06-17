package com.yuntanfang.module.message.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.message.entity.Message;
import com.yuntanfang.module.message.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;

    @Transactional
    public Message create(Long userId, String role, String targetType, String title, String content, String bizType, Long bizId) {
        Message message = new Message();
        message.setUserId(userId);
        message.setRole(role);
        message.setTargetType(targetType == null ? "user" : targetType);
        message.setTitle(title);
        message.setContent(content);
        message.setBizType(bizType);
        message.setBizId(bizId);
        message.setStatus("unread");
        messageMapper.insert(message);
        return message;
    }

    public PageResult<Message> my(Long userId, String role) {
        if (userId == null) {
            throw new BusinessException("未登录");
        }
        List<Message> records = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .and(wrapper -> wrapper.eq(Message::getUserId, userId)
                        .or().eq(Message::getRole, role == null ? "" : role))
                .orderByDesc(Message::getId));
        return PageResult.of(records);
    }

    @Transactional
    public Message markRead(Long userId, Long id) {
        Message message = messageMapper.selectById(id);
        if (message == null || (message.getUserId() != null && !message.getUserId().equals(userId))) {
            throw new BusinessException("消息不存在");
        }
        message.setStatus("read");
        messageMapper.updateById(message);
        return message;
    }
}
