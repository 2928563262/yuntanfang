package com.yuntanfang.module.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MockRecords {

    private MockRecords() {
    }

    public static Map<String, Object> record(Object id, String name, String status) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("status", status);
        return data;
    }

    public static List<Map<String, Object>> list(String module) {
        return List.of(record(1L, module + "示例", "mock"));
    }
}
