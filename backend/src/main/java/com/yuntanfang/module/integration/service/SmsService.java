package com.yuntanfang.module.integration.service;

public interface SmsService {

    boolean sendCode(String mobile, String code);
}
