package com.yuntanfang.module.integration.service.impl;

import com.yuntanfang.module.integration.service.ObjectStorageService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MockObjectStorageService implements ObjectStorageService {

    @Override
    public Map<String, Object> createUploadUrl(String fileName, String contentType) {
        return Map.of("provider", "mock", "uploadUrl", "https://oss.example.com/upload/" + fileName, "accessUrl", "https://oss.example.com/" + fileName);
    }
}
