package com.yuntanfang.module.integration.service.impl;

import com.yuntanfang.module.integration.service.MapService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MockMapService implements MapService {

    @Override
    public Map<String, Object> route(double fromLat, double fromLng, double toLat, double toLng) {
        return Map.of("provider", "mock", "distanceMeters", 1200, "url", "https://map.example.com/mock-route");
    }
}
