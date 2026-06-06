package com.yuntanfang.module.integration.service;

import java.util.Map;

public interface MapService {

    Map<String, Object> route(double fromLat, double fromLng, double toLat, double toLng);
}
