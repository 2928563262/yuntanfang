package com.yuntanfang.module.integration.service.impl;

import com.yuntanfang.module.integration.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class MockPaymentService implements PaymentService {

    @Override
    public Map<String, Object> createH5Payment(Long orderId, BigDecimal amount) {
        return Map.of("provider", "mock", "orderId", orderId, "amount", amount, "payUrl", "https://pay.example.com/mock");
    }

    @Override
    public Map<String, Object> refund(Long paymentId) {
        return Map.of("provider", "mock", "paymentId", paymentId, "status", "refunded");
    }
}
