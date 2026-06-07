package com.yuntanfang.module.agent.vo;

import java.util.List;

public record AgentOrderResult(
        String reply,
        String stallName,
        List<OrderItem> items,
        String pickupTime,
        String totalAmount,
        String confidence,
        String status,
        String rawModelOutput
) {
    public record OrderItem(String name, int quantity, String note) {
    }
}
