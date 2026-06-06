package com.yuntanfang.module.integration.service;

import java.util.Map;

public interface ContentSafetyService {

    Map<String, Object> auditText(String content);
}
