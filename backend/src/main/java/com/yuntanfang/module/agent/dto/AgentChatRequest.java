package com.yuntanfang.module.agent.dto;

import java.util.List;
import java.util.Map;

public record AgentChatRequest(
        String message,
        List<String> history,
        Map<String, Object> context
) {
}
