package com.yuntanfang.module.agent.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.module.agent.dto.AgentChatRequest;
import com.yuntanfang.module.agent.service.AgentAssistantService;
import com.yuntanfang.module.agent.vo.AgentChatResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentAssistantService agentAssistantService;

    public AgentController(AgentAssistantService agentAssistantService) {
        this.agentAssistantService = agentAssistantService;
    }

    @PostMapping("/chat")
    public ApiResponse<AgentChatResult> chat(@RequestBody AgentChatRequest request) {
        return ApiResponse.ok(agentAssistantService.chat(request));
    }
}
