package com.yuntanfang.module.agent.vo;

import java.util.List;
import java.util.Map;

public record AgentChatResult(
        String reply,
        String intent,
        AgentAction action,
        List<Map<String, Object>> cards,
        List<ProcessStep> processSteps,
        List<String> suggestedPrompts,
        String status,
        String rawModelOutput
) {
    public record AgentAction(
            String type,
            String label,
            String route,
            Map<String, Object> payload
    ) {
    }

    public record ProcessStep(
            String title,
            String status,
            String detail
    ) {
    }
}
