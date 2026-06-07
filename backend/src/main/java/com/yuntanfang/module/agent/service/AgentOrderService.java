package com.yuntanfang.module.agent.service;

import com.yuntanfang.module.agent.dto.AgentOrderRequest;
import com.yuntanfang.module.agent.vo.AgentOrderResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class AgentOrderService {

    private final RestClient restClient;
    private final String apiKey;
    private final String model;

    public AgentOrderService(
            @Value("${app.deepseek.base-url:https://api.deepseek.com}") String baseUrl,
            @Value("${app.deepseek.api-key:}") String apiKey,
            @Value("${app.deepseek.model:deepseek-v4-pro}") String model
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.model = model;
    }

    public AgentOrderResult parse(AgentOrderRequest request) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallback(request.message(), "fallback_no_api_key");
        }

        try {
            Map<String, Object> response = restClient.post()
                    .uri("/v1/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(buildPayload(request))
                    .retrieve()
                    .body(Map.class);

            String content = extractContent(response);
            return toResult(request.message(), content, "deepseek");
        } catch (Exception ex) {
            return fallback(request.message(), "fallback_deepseek_error");
        }
    }

    private Map<String, Object> buildPayload(AgentOrderRequest request) {
        return Map.of(
                "model", model,
                "temperature", 0.2,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", """
                                        你是云摊坊智能点单助手。只处理摊位预约点单。
                                        根据用户输入抽取：摊位名、商品、数量、口味备注、取餐时间。
                                        若信息不足，先追问。若可下单，返回简短中文确认。
                                        当前可用摊位：烟火小摊、乡野新农人鲜铺、守艺糖画铺。
                                        当前商品：招牌汤粉、手作糍粑、冰糖绿豆沙、当季蔬果、农家土鸡蛋、手工辣酱、生肖糖画、定制糖牌、糖画体验。
                                        """
                        ),
                        Map.of("role", "user", "content", request.message() == null ? "" : request.message())
                )
        );
    }

    @SuppressWarnings("unchecked")
    private String extractContent(Map<String, Object> response) {
        if (response == null) {
            return "";
        }
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        if (choices == null || choices.isEmpty()) {
            return "";
        }
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return message == null ? "" : String.valueOf(message.getOrDefault("content", ""));
    }

    private AgentOrderResult toResult(String input, String reply, String status) {
        AgentOrderResult fallback = fallback(input, status);
        return new AgentOrderResult(
                reply == null || reply.isBlank() ? fallback.reply() : reply,
                fallback.stallName(),
                fallback.items(),
                fallback.pickupTime(),
                fallback.totalAmount(),
                fallback.confidence(),
                status,
                reply
        );
    }

    private AgentOrderResult fallback(String input, String status) {
        String text = input == null ? "" : input;
        String stallName = text.contains("糖画") ? "守艺糖画铺" : text.contains("鲜") || text.contains("鸡蛋") ? "乡野新农人鲜铺" : "烟火小摊";
        String product = text.contains("糖画") ? "生肖糖画" : text.contains("鸡蛋") ? "农家土鸡蛋" : "招牌汤粉";
        int quantity = text.contains("两") || text.contains("2") ? 2 : 1;
        String pickupTime = text.contains("18:30") ? "今天 18:30" : "今天 19:00";
        String total = String.format("%.2f", quantity * 16.0);
        return new AgentOrderResult(
                "已识别：" + stallName + "，" + product + " x" + quantity + "，建议确认后生成预约订单。",
                stallName,
                List.of(new AgentOrderResult.OrderItem(product, quantity, "")),
                pickupTime,
                total,
                status.startsWith("fallback") ? "0.72" : "0.92",
                status,
                ""
        );
    }
}
