package com.yuntanfang.module.integration.service.impl;

import com.yuntanfang.module.integration.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class MockNotificationService implements NotificationService {

    @Override
    public boolean notifyUser(Long userId, String title, String content) {
        return true;
    }
}
