package com.yuntanfang.module.integration.service;

public interface NotificationService {

    boolean notifyUser(Long userId, String title, String content);
}
