package com.yuntanfang.module.integration.service.impl;

import com.yuntanfang.module.integration.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class MockSmsService implements SmsService {

    @Override
    public boolean sendCode(String mobile, String code) {
        return true;
    }
}
