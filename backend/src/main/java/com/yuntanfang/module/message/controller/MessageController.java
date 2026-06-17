package com.yuntanfang.module.message.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.common.PageResult;
import com.yuntanfang.module.message.entity.Message;
import com.yuntanfang.module.message.service.MessageService;
import com.yuntanfang.security.AuthSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final AuthSupport authSupport;

    @GetMapping("/my")
    public ApiResponse<PageResult<Message>> my(@RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(messageService.my(authSupport.currentUserId(authorization), authSupport.currentRole(authorization)));
    }

    @PutMapping("/{id}/read")
    public ApiResponse<Message> read(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return ApiResponse.ok(messageService.markRead(authSupport.currentUserId(authorization), id));
    }
}
