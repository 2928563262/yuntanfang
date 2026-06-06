package com.yuntanfang.module.integration.service;

import java.util.Map;

public interface ObjectStorageService {

    Map<String, Object> createUploadUrl(String fileName, String contentType);
}
