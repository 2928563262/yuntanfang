package com.yuntanfang.module.integration.service;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentService {

    Map<String, Object> createH5Payment(Long orderId, BigDecimal amount);

    Map<String, Object> refund(Long paymentId);
}
