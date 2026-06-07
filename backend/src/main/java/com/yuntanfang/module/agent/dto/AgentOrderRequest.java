package com.yuntanfang.module.agent.dto;

import java.util.List;

public record AgentOrderRequest(String message, List<String> history) {
}
