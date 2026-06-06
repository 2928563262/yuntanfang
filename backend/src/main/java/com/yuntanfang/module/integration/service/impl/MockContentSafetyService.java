package com.yuntanfang.module.integration.service.impl;

import com.yuntanfang.module.integration.service.ContentSafetyService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MockContentSafetyService implements ContentSafetyService {

    @Override
    public Map<String, Object> auditText(String content) {
        return Map.of("provider", "mock", "suggestion", "pass");
    }
}
