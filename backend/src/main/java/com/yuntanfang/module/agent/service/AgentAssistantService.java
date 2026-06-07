package com.yuntanfang.module.agent.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuntanfang.module.agent.dto.AgentChatRequest;
import com.yuntanfang.module.agent.vo.AgentChatResult;
import com.yuntanfang.module.agent.vo.AgentChatResult.AgentAction;
import com.yuntanfang.module.agent.vo.AgentChatResult.ProcessStep;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AgentAssistantService {

    private static final List<Map<String, Object>> STALLS = List.of(
            stall(1, "烟火小摊", "林师傅", "地方特色", "营业中", "680m", "4.8", "北站中心公园东门",
                    List.of("招牌汤粉", "手作糍粑", "冰糖绿豆沙")),
            stall(2, "乡野新农人鲜铺", "陈小禾", "农家特产", "今日推荐", "1.2km", "4.7", "市民广场南侧临时摊区",
                    List.of("当季蔬果", "农家土鸡蛋", "手工辣酱")),
            stall(3, "守艺糖画铺", "周老师", "非遗好物", "即将出摊", "2.0km", "4.9", "老街口文创夜市",
                    List.of("生肖糖画", "定制糖牌", "糖画体验"))
    );

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public AgentAssistantService(
            @Value("${app.deepseek.base-url:https://api.deepseek.com}") String baseUrl,
            @Value("${app.deepseek.api-key:}") String apiKey,
            @Value("${app.deepseek.model:deepseek-v4-pro}") String model,
            ObjectMapper objectMapper
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.model = model;
        this.objectMapper = objectMapper;
    }

    public AgentChatResult chat(AgentChatRequest request) {
        String message = safe(request.message());
        AgentDecision decision = classifyWithModel(message, request);
        if (decision == null) {
            return unavailable();
        }
        return execute(decision, message);
    }

    private AgentDecision classifyWithModel(String message, AgentChatRequest request) {
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                Map<String, Object> response = restClient.post()
                        .uri("/v1/chat/completions")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of(
                                "model", model,
                                "temperature", 0.1,
                                "messages", List.of(
                                        Map.of("role", "system", "content", systemPrompt()),
                                        Map.of("role", "user", "content", buildUserPrompt(message, request))
                                )
                        ))
                        .retrieve()
                        .body(Map.class);
                String content = extractContent(response);
                AgentDecision decision = parseDecision(content);
                if (decision != null) {
                    return decision;
                }
            } catch (Exception ex) {
                if (attempt == 3) {
                    return null;
                }
            }
        }
        return null;
    }

    private String systemPrompt() {
        return """
                你是云摊坊系统 Agent。你的任务是理解用户想做什么，并选择一个白名单 tool。
                只能返回 JSON，不要返回 markdown，不要解释。
                JSON 格式：
                {"intent":"search_stalls|create_order|submit_review|submit_complaint|system_help","parameters":{...}}
                可用 tool：
                1. search_stalls: 参数 keyword, category。至少需要 keyword 或 category，"附近摊位"这种泛泛表达不算充分。
                2. create_order: 参数 stallName, productName, quantity, pickupTime, contact。必须有用户明确说出的 productName；只有摊位名或只说预约时不能下单。
                3. submit_review: 参数 orderId, rating, content。至少需要 rating 或 content。
                4. submit_complaint: 参数 target, type, description。至少需要 target 或 description。
                5. system_help: 参数 topic。
                已知摊位：烟火小摊、乡野新农人鲜铺、守艺糖画铺。
                已知商品：招牌汤粉、手作糍粑、冰糖绿豆沙、当季蔬果、农家土鸡蛋、手工辣酱、生肖糖画、定制糖牌、糖画体验。
                当用户要执行 tool 但必需参数不足时，仍返回对应 intent，只填已知参数；后端会追问，不要编造参数。不要根据摊位默认补商品。
                若用户只是问怎么用、入口在哪、能做什么，使用 system_help。
                """;
    }

    private String buildUserPrompt(String message, AgentChatRequest request) {
        return "用户输入：" + message + "\n历史：" + String.join(" | ", request.history() == null ? List.of() : request.history());
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

    private AgentDecision parseDecision(String content) {
        if (content == null || content.isBlank()) {
            return null;
        }
        String json = content.trim();
        if (json.startsWith("```")) {
            json = json.replaceFirst("```json", "").replaceFirst("```", "").replace("```", "").trim();
        }
        try {
            Map<String, Object> data = objectMapper.readValue(json, new TypeReference<>() {
            });
            String intent = safe(data.get("intent"));
            Map<String, Object> parameters = asMap(data.get("parameters"));
            if (intent.isBlank()) {
                return null;
            }
            return new AgentDecision(intent, parameters, content);
        } catch (Exception ex) {
            return null;
        }
    }

    private AgentChatResult execute(AgentDecision decision, String message) {
        String intent = normalizeIntent(decision.intent());
        Map<String, Object> p = decision.parameters();
        AgentChatResult clarification = validateRequired(intent, p, message);
        if (clarification != null) {
            return clarification;
        }
        return switch (intent) {
            case "search_stalls" -> searchStalls(p, decision.raw());
            case "create_order" -> createOrder(p, decision.raw());
            case "submit_review" -> submitReview(p, decision.raw());
            case "submit_complaint" -> submitComplaint(p, decision.raw());
            default -> systemHelp(p, decision.raw(), message);
        };
    }

    private AgentChatResult searchStalls(Map<String, Object> parameters, String raw) {
        String keyword = safe(parameters.get("keyword")).toLowerCase(Locale.ROOT);
        String category = safe(parameters.get("category"));
        List<Map<String, Object>> records = STALLS.stream()
                .filter(stall -> category.isBlank() || category.equals(stall.get("category")))
                .filter(stall -> keyword.isBlank() || String.join(" ", flatValues(stall)).toLowerCase(Locale.ROOT).contains(keyword))
                .limit(3)
                .toList();
        if (records.isEmpty()) {
            records = category.isBlank()
                    ? STALLS
                    : STALLS.stream().filter(stall -> category.equals(stall.get("category"))).toList();
        }
        String summary = records.stream()
                .map(stall -> stall.get("name") + "：" + stall.get("category") + "，" + stall.get("status")
                        + "，" + stall.get("distance") + "，评分 " + stall.get("rating") + "，地址 " + stall.get("address"))
                .reduce((a, b) -> a + "；" + b)
                .orElse("暂无匹配摊位");
        return result(
                "已找到 " + records.size() + " 个摊位。" + summary + "。",
                "search_stalls",
                new AgentAction("open_route", "查看摊位列表", "/stalls", Map.of("keyword", keyword, "category", category)),
                records,
                raw
        );
    }

    private AgentChatResult createOrder(Map<String, Object> parameters, String raw) {
        String stallName = blankToDefault(safe(parameters.get("stallName")), "烟火小摊");
        String productName = blankToDefault(safe(parameters.get("productName")), "招牌汤粉");
        int quantity = Math.max(1, toInt(parameters.get("quantity"), 1));
        String pickupTime = blankToDefault(safe(parameters.get("pickupTime")), "今天 19:00");
        String amount = String.format(Locale.ROOT, "%.2f", quantity * 16.0);
        Map<String, Object> card = new LinkedHashMap<>();
        card.put("orderId", nextMockId());
        card.put("stallName", stallName);
        card.put("productName", productName);
        card.put("quantity", quantity);
        card.put("pickupTime", pickupTime);
        card.put("totalAmount", amount);
        card.put("status", "待支付");
        return result(
                "已生成预约单草稿：" + stallName + "，" + productName + " x" + quantity + "，" + pickupTime + " 取。你可以确认后到订单详情支付。",
                "create_order",
                new AgentAction("create_order", "确认创建订单", "/orders", card),
                List.of(card),
                raw
        );
    }

    private AgentChatResult submitReview(Map<String, Object> parameters, String raw) {
        int orderId = toInt(parameters.get("orderId"), 1002);
        int rating = Math.min(5, Math.max(1, toInt(parameters.get("rating"), 5)));
        String content = blankToDefault(safe(parameters.get("content")), "整体体验不错，取餐流程顺畅。");
        Map<String, Object> card = Map.of(
                "reviewId", nextMockId(),
                "orderId", orderId,
                "rating", rating,
                "content", content,
                "status", "已发布"
        );
        return result(
                "评价已整理好，评分 " + rating + " 星。确认后会进入我的评价，并展示到对应摊位。",
                "submit_review",
                new AgentAction("submit_review", "确认发布评价", "/my-reviews", card),
                List.of(card),
                raw
        );
    }

    private AgentChatResult submitComplaint(Map<String, Object> parameters, String raw) {
        String target = blankToDefault(safe(parameters.get("target")), "烟火小摊");
        String type = blankToDefault(safe(parameters.get("type")), "服务问题");
        String description = blankToDefault(safe(parameters.get("description")), "用户通过 Agent 提交投诉。");
        Map<String, Object> card = Map.of(
                "complaintId", nextMockId(),
                "target", target,
                "type", type,
                "description", description,
                "status", "处理中"
        );
        return result(
                "投诉工单已整理：" + target + "，类型为" + type + "。确认后可在我的投诉查看处理进度。",
                "submit_complaint",
                new AgentAction("submit_complaint", "确认提交投诉", "/complaints", card),
                List.of(card),
                raw
        );
    }

    private AgentChatResult systemHelp(Map<String, Object> parameters, String raw, String message) {
        String topic = safe(parameters.get("topic"));
        String reply;
        String route = "/";
        if (containsAny(topic + message, "订单", "支付", "取餐")) {
            reply = "订单入口在底部“订单”或个人中心“我的订单”。你可以查看取餐码、支付状态、退款和评价入口。";
            route = "/orders";
        } else if (containsAny(topic + message, "投诉", "举报")) {
            reply = "投诉入口在个人中心“我的投诉”，也可以在摊位详情右上角进入。提交后可查看处理进度。";
            route = "/complaints/create";
        } else if (containsAny(topic + message, "收藏", "提醒")) {
            reply = "收藏和出摊提醒在摊位详情操作，管理入口在个人中心的“收藏关注”和“出摊提醒”。";
            route = "/favorites";
        } else {
            reply = "我可以帮你找摊位、预约下单、提交评价、投诉举报，也可以告诉你各功能入口。你可以直接说想做什么。";
        }
        Map<String, Object> card = Map.of("topic", blankToDefault(topic, "overview"), "route", route);
        return result(
                reply,
                "system_help",
                new AgentAction("open_route", "打开相关入口", route, card),
                List.of(card),
                raw
        );
    }

    private AgentChatResult result(String reply, String intent, AgentAction action, List<Map<String, Object>> cards, String raw) {
        return new AgentChatResult(
                reply,
                intent,
                action,
                cards,
                processSteps(intent, "completed", cards.size()),
                List.of("找地方特色摊位", "预约一份招牌汤粉", "给上一单好评", "投诉卫生问题"),
                "deepseek",
                raw == null ? "" : raw
        );
    }

    private AgentChatResult validateRequired(String intent, Map<String, Object> parameters, String message) {
        return switch (intent) {
            case "search_stalls" -> insufficient(
                    (safe(parameters.get("keyword")).isBlank() || isGenericSearchKeyword(safe(parameters.get("keyword"))))
                            && safe(parameters.get("category")).isBlank(),
                    "你想找哪类摊位？可以说地方特色、农家特产、非遗好物，或告诉我具体商品/位置。",
                    "search_stalls",
                    List.of("找地方特色摊位", "找农家特产", "找非遗好物")
            );
            case "create_order" -> insufficient(
                    safe(parameters.get("productName")).isBlank() || !hasExplicitProduct(message, safe(parameters.get("productName"))),
                    "你想预约哪个商品？比如招牌汤粉、农家土鸡蛋、生肖糖画。请先告诉我具体商品，再帮你生成订单。",
                    "create_order",
                    List.of("预约招牌汤粉", "预约农家土鸡蛋", "预约生肖糖画")
            );
            case "submit_review" -> insufficient(
                    safe(parameters.get("rating")).isBlank() && safe(parameters.get("content")).isBlank(),
                    "你想给几星？也可以直接说评价内容，比如服务很好、取餐很快。",
                    "submit_review",
                    List.of("给 5 星好评", "服务很好", "取餐很快")
            );
            case "submit_complaint" -> insufficient(
                    safe(parameters.get("target")).isBlank() && safe(parameters.get("description")).isBlank(),
                    "你要投诉哪个摊位或问题？请告诉我对象，或简单描述发生了什么。",
                    "submit_complaint",
                    List.of("投诉烟火小摊卫生问题", "投诉占道经营", "投诉服务态度")
            );
            default -> null;
        };
    }

    private AgentChatResult insufficient(boolean condition, String reply, String intent, List<String> prompts) {
        if (!condition) {
            return null;
        }
        return new AgentChatResult(
                reply,
                intent,
                new AgentAction("ask_clarification", "补充信息", "", Map.of()),
                List.of(),
                processSteps(intent, "waiting_input", 0),
                prompts,
                "need_more_info",
                ""
        );
    }

    private AgentChatResult unavailable() {
        return new AgentChatResult(
                "Agent 功能暂时不可用，请稍后再试。",
                "unavailable",
                new AgentAction("none", "功能暂时不可用", "", Map.of()),
                List.of(),
                List.of(
                        new ProcessStep("连接模型服务", "failed", "已重试 3 次"),
                        new ProcessStep("终止执行", "completed", "未调用任何功能 API")
                ),
                List.of("稍后重试", "手动浏览摊位"),
                "unavailable",
                ""
        );
    }

    private List<ProcessStep> processSteps(String intent, String status, int cardCount) {
        return List.of(
                new ProcessStep("识别用户意图", "completed", intent),
                new ProcessStep("检查必需参数", status.equals("waiting_input") ? "waiting_input" : "completed",
                        status.equals("waiting_input") ? "等待用户补充" : "参数满足"),
                new ProcessStep("调用功能 API", status.equals("waiting_input") ? "pending" : "completed",
                        status.equals("waiting_input") ? "未调用" : "返回 " + cardCount + " 条结果")
        );
    }

    private static Map<String, Object> stall(int id, String name, String vendor, String category, String status, String distance,
                                             String rating, String address, List<String> products) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("vendor", vendor);
        data.put("category", category);
        data.put("status", status);
        data.put("distance", distance);
        data.put("rating", rating);
        data.put("address", address);
        data.put("products", products);
        return data;
    }

    private static List<String> flatValues(Map<String, Object> data) {
        List<String> values = new ArrayList<>();
        data.values().forEach(value -> values.add(String.valueOf(value)));
        return values;
    }

    private static String normalizeIntent(String intent) {
        return switch (safe(intent)) {
            case "search_stalls", "create_order", "submit_review", "submit_complaint" -> intent;
            default -> "system_help";
        };
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> asMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            map.forEach((k, v) -> result.put(String.valueOf(k), v));
            return result;
        }
        return new LinkedHashMap<>();
    }

    private static boolean containsAny(String text, String... words) {
        String value = text == null ? "" : text;
        for (String word : words) {
            if (value.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isGenericSearchKeyword(String keyword) {
        String value = keyword.replaceAll("\\s+", "");
        return value.isBlank()
                || value.equals("附近摊位")
                || value.equals("摊位")
                || value.equals("找摊位")
                || value.equals("帮我找摊位")
                || value.equals("帮我找附近摊位");
    }

    private static boolean hasExplicitProduct(String message, String productName) {
        String text = message == null ? "" : message;
        if (productName.isBlank()) {
            return false;
        }
        if (text.contains(productName)) {
            return true;
        }
        return switch (productName) {
            case "招牌汤粉" -> containsAny(text, "汤粉", "粉");
            case "手作糍粑" -> containsAny(text, "糍粑");
            case "冰糖绿豆沙" -> containsAny(text, "绿豆沙", "饮品");
            case "当季蔬果" -> containsAny(text, "蔬果", "水果", "蔬菜");
            case "农家土鸡蛋" -> containsAny(text, "鸡蛋", "土鸡蛋");
            case "手工辣酱" -> containsAny(text, "辣酱");
            case "生肖糖画" -> containsAny(text, "糖画");
            case "定制糖牌" -> containsAny(text, "糖牌");
            case "糖画体验" -> containsAny(text, "糖画体验", "体验");
            default -> false;
        };
    }

    private static String inferStallName(String text) {
        if (containsAny(text, "糖画", "周老师", "非遗")) {
            return "守艺糖画铺";
        }
        if (containsAny(text, "鲜", "鸡蛋", "蔬果", "农家")) {
            return "乡野新农人鲜铺";
        }
        return "烟火小摊";
    }

    private static String inferProductName(String text) {
        if (containsAny(text, "糖画")) {
            return "生肖糖画";
        }
        if (containsAny(text, "鸡蛋")) {
            return "农家土鸡蛋";
        }
        if (containsAny(text, "糍粑")) {
            return "手作糍粑";
        }
        return "招牌汤粉";
    }

    private static String inferCategory(String text) {
        if (containsAny(text, "农家", "蔬果", "鸡蛋")) {
            return "农家特产";
        }
        if (containsAny(text, "糖画", "非遗")) {
            return "非遗好物";
        }
        if (containsAny(text, "特色", "汤粉")) {
            return "地方特色";
        }
        return "";
    }

    private static int inferNumber(String text, int fallback) {
        String digits = text == null ? "" : text.replaceAll("\\D+", "");
        if (digits.isBlank()) {
            return fallback;
        }
        return toInt(digits, fallback);
    }

    private static int toInt(Object value, int fallback) {
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception ex) {
            return fallback;
        }
    }

    private static String safe(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private static String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static int nextMockId() {
        String value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        return Integer.parseInt(value);
    }

    private record AgentDecision(String intent, Map<String, Object> parameters, String raw) {
    }
}
