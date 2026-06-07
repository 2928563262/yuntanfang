package com.yuntanfang.module.agent.controller;

import com.yuntanfang.common.ApiResponse;
import com.yuntanfang.module.agent.dto.AgentOrderRequest;
import com.yuntanfang.module.agent.service.AgentOrderService;
import com.yuntanfang.module.agent.vo.AgentOrderResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agent/order")
public class AgentOrderController {

    private final AgentOrderService agentOrderService;

    public AgentOrderController(AgentOrderService agentOrderService) {
        this.agentOrderService = agentOrderService;
    }

    @PostMapping("/parse")
    public ApiResponse<AgentOrderResult> parse(@RequestBody AgentOrderRequest request) {
        return ApiResponse.ok(agentOrderService.parse(request));
    }
}
