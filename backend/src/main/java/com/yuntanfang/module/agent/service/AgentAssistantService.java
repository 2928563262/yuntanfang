package com.yuntanfang.module.agent.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuntanfang.module.agent.dto.AgentChatRequest;
import com.yuntanfang.module.agent.vo.AgentChatResult;
import com.yuntanfang.module.agent.vo.AgentChatResult.AgentAction;
import com.yuntanfang.module.agent.vo.AgentChatResult.ProcessStep;
import com.yuntanfang.module.product.entity.Product;
import com.yuntanfang.module.product.mapper.ProductMapper;
import com.yuntanfang.module.stall.entity.Stall;
import com.yuntanfang.module.stall.mapper.StallMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class AgentAssistantService {

    private static final List<Map<String, Object>> STALLS = List.of(
            stall(1, "烟火小摊", "林师傅", "地方特色", "营业中", "680m", "4.8", "北站中心公园东门",
                    List.of("招牌烤串(10串)", "现炸薯条", "鲜榨果汁")),
            stall(2, "乡野新农人鲜铺", "陈小禾", "农家特产", "今日推荐", "1.2km", "4.7", "市民广场南侧临时摊区",
                    List.of("招牌烤串(10串)", "现炸薯条", "鲜榨果汁")),
            stall(3, "非遗手作摊", "周老师", "非遗好物", "即将出摊", "2.0km", "4.9", "老街口文创夜市",
                    List.of("手作体验", "文创小物"))
    );

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final ProductMapper productMapper;
    private final StallMapper stallMapper;
    private final String apiKey;
    private final String model;

    public AgentAssistantService(
            @Value("${app.deepseek.base-url:https://api.deepseek.com}") String baseUrl,
            @Value("${app.deepseek.api-key:}") String apiKey,
            @Value("${app.deepseek.model:deepseek-v4-pro}") String model,
            ObjectMapper objectMapper,
            ProductMapper productMapper,
            StallMapper stallMapper
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.model = model;
        this.objectMapper = objectMapper;
        this.productMapper = productMapper;
        this.stallMapper = stallMapper;
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
                全局规则：只抽取用户当前输入或历史中明确确认过的参数；不要猜测、不要默认补齐。缺必需参数时仍返回对应 intent 和已知参数，后端会追问。
                1. search_stalls: 参数 keyword, category。必须有具体 keyword 或 category；"附近摊位"这种泛泛表达不算充分。
                2. create_order: 参数 stallName, productName, quantity, pickupTime, contact。必须有用户明确说出的 productName；不能根据摊位默认补商品。
                3. submit_review: 参数 orderId, rating, content。必须有订单指代（orderId 或 上一单/最近一单）且必须有明确评分信号（数字星级/好评/中评/差评/一般等）。
                4. submit_complaint: 参数 target, type, description。必须有投诉对象 target，且必须有 type 或 description。
                5. system_help: 参数 topic。
                已知摊位：烟火小摊、乡野新农人鲜铺、守艺糖画铺。
                已知商品：招牌烤串(10串)、现炸薯条、鲜榨果汁。
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
            case "submit_review" -> submitReview(p, decision.raw(), message);
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
        String productName = safe(parameters.get("productName"));
        Product product = findProduct(productName);
        Stall stall = findStallForProduct(product);
        String stallName = stall == null ? blankToDefault(safe(parameters.get("stallName")), "烟火小摊") : stall.getStallName();
        int quantity = Math.max(1, toInt(parameters.get("quantity"), 1));
        String pickupTime = blankToDefault(safe(parameters.get("pickupTime")), "今天 19:00");
        String price = product.getPrice() == null ? "0.00" : product.getPrice().toPlainString();
        String amount = product.getPrice() == null
                ? "0.00"
                : product.getPrice().multiply(java.math.BigDecimal.valueOf(quantity)).toPlainString();
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("productId", product.getId());
        item.put("productName", product.getProductName());
        item.put("quantity", quantity);
        item.put("price", price);
        Map<String, Object> orderPayload = new LinkedHashMap<>();
        orderPayload.put("vendorId", product.getVendorId());
        orderPayload.put("stallId", stall == null ? null : stall.getId());
        orderPayload.put("stallName", stallName);
        orderPayload.put("pickupTime", pickupTime);
        orderPayload.put("contactPhone", "");
        orderPayload.put("remark", "Agent 创建订单草稿");
        orderPayload.put("items", List.of(item));
        Map<String, Object> card = new LinkedHashMap<>();
        card.put("productId", product.getId());
        card.put("vendorId", product.getVendorId());
        card.put("stallId", stall == null ? null : stall.getId());
        card.put("stallName", stallName);
        card.put("productName", product.getProductName());
        card.put("quantity", quantity);
        card.put("pickupTime", pickupTime);
        card.put("price", price);
        card.put("totalAmount", amount);
        card.put("status", "待支付");
        card.put("orderPayload", orderPayload);
        return result(
                "已生成预约单草稿：" + stallName + "，" + product.getProductName() + " x" + quantity + "，" + pickupTime + " 取。确认后会创建真实订单，用户端和商家端都能看到。",
                "create_order",
                new AgentAction("create_order", "确认创建订单", "/orders", card),
                List.of(card),
                raw
        );
    }

    private AgentChatResult submitReview(Map<String, Object> parameters, String raw, String message) {
        int orderId = toInt(parameters.get("orderId"), recentOrderId(message));
        int rating = ratingFrom(parameters, message);
        String content = blankToDefault(safe(parameters.get("content")), defaultReviewContent(rating));
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
        String target = safe(parameters.get("target"));
        String type = blankToDefault(safe(parameters.get("type")), "其他问题");
        String description = safe(parameters.get("description"));
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
                List.of("找地方特色摊位", "预约一份招牌烤串", "给上一单好评", "投诉烟火小摊卫生问题"),
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
                    !isValidOrderProduct(parameters, message),
                    "你想预约哪个商品？目前可预约招牌烤串(10串)、现炸薯条、鲜榨果汁。请先告诉我具体商品，再帮你生成订单。",
                    "create_order",
                    List.of("预约招牌烤串", "预约现炸薯条", "预约鲜榨果汁")
            );
            case "submit_review" -> insufficient(
                    !hasOrderReference(parameters, message) || !hasRatingSignal(parameters, message),
                    reviewClarification(parameters, message),
                    "submit_review",
                    List.of("给上一单 5 星好评", "评价订单 1002 服务很好", "最近一单取餐很快")
            );
            case "submit_complaint" -> insufficient(
                    safe(parameters.get("target")).isBlank()
                            || (safe(parameters.get("type")).isBlank() && safe(parameters.get("description")).isBlank()),
                    complaintClarification(parameters),
                    "submit_complaint",
                    List.of("投诉烟火小摊卫生问题", "投诉守艺糖画铺占道经营", "投诉订单 1002 商品质量")
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

    private boolean isValidOrderProduct(Map<String, Object> parameters, String message) {
        String productName = safe(parameters.get("productName"));
        if (productName.isBlank()) {
            return false;
        }
        Product product = findProduct(productName);
        return product != null && hasExplicitProduct(message, product.getProductName());
    }

    private static boolean hasExplicitProduct(String message, String productName) {
        String text = message == null ? "" : message;
        if (productName.isBlank()) {
            return false;
        }
        if (isNegatedProduct(text, productName)) {
            return false;
        }
        if (text.contains(productName)) {
            return true;
        }
        return switch (productName) {
            case "招牌烤串(10串)" -> containsAny(text, "烤串", "串");
            case "现炸薯条" -> containsAny(text, "薯条");
            case "鲜榨果汁" -> containsAny(text, "果汁", "饮品");
            default -> false;
        };
    }

    private static boolean isNegatedProduct(String message, String productName) {
        return containsAny(message, "没说", "不是", "不要", "不想", "别")
                && (message.contains(productName) || switch (productName) {
                    case "招牌烤串(10串)" -> containsAny(message, "烤串", "串");
                    case "现炸薯条" -> containsAny(message, "薯条");
                    case "鲜榨果汁" -> containsAny(message, "果汁", "饮品");
                    default -> false;
                });
    }

    private static boolean hasOrderReference(Map<String, Object> parameters, String message) {
        return toInt(parameters.get("orderId"), 0) > 0 || recentOrderId(message) > 0;
    }

    private static int recentOrderId(String message) {
        return containsAny(message, "上一单", "最近一单", "刚才那单", "这单", "这个订单") ? 1002 : 0;
    }

    private static String reviewClarification(Map<String, Object> parameters, String message) {
        if (!hasOrderReference(parameters, message)) {
            return "你想评价哪个订单？可以说订单号，或明确说上一单、最近一单。";
        }
        return "你想给几星？可以说 1-5 星，或明确说好评、中评、差评。";
    }

    private static String complaintClarification(Map<String, Object> parameters) {
        if (safe(parameters.get("target")).isBlank()) {
            return "你要投诉哪个摊位、订单或摊区？请先告诉我投诉对象。";
        }
        return "你要投诉的具体问题是什么？可以说卫生问题、占道经营、商品质量或服务态度。";
    }

    private static boolean hasRatingSignal(Map<String, Object> parameters, String message) {
        return toInt(parameters.get("rating"), 0) > 0
                || containsAny(message, "好评", "五星", "5星", "五分", "中评", "一般", "差评", "一星", "1星", "不好", "很差");
    }

    private static int ratingFrom(Map<String, Object> parameters, String message) {
        int rating = toInt(parameters.get("rating"), 0);
        if (rating > 0) {
            return Math.min(5, Math.max(1, rating));
        }
        if (containsAny(message, "差评", "一星", "1星", "不好", "很差")) {
            return 1;
        }
        if (containsAny(message, "中评", "一般")) {
            return 3;
        }
        return 5;
    }

    private static String defaultReviewContent(int rating) {
        if (rating <= 2) {
            return "本次体验不满意，希望后续改进。";
        }
        if (rating == 3) {
            return "本次体验一般，还有改进空间。";
        }
        return "整体体验不错，取餐流程顺畅。";
    }

    private Product findProduct(String productName) {
        String query = safe(productName);
        if (query.isBlank()) {
            return null;
        }
        return productMapper.selectList(new LambdaQueryWrapper<Product>()
                        .eq(Product::getStatus, "on_sale"))
                .stream()
                .filter(product -> product.getProductName() != null)
                .filter(product -> product.getProductName().contains(query)
                        || query.contains(product.getProductName())
                        || productAliasMatches(query, product.getProductName()))
                .min(Comparator.comparing(product -> product.getProductName().length()))
                .orElse(null);
    }

    private Stall findStallForProduct(Product product) {
        if (product == null || product.getVendorId() == null) {
            return null;
        }
        return stallMapper.selectList(new LambdaQueryWrapper<Stall>()
                        .eq(Stall::getVendorId, product.getVendorId())
                        .orderByAsc(Stall::getId))
                .stream()
                .findFirst()
                .orElse(null);
    }

    private static boolean productAliasMatches(String query, String productName) {
        return switch (productName) {
            case "招牌烤串(10串)" -> containsAny(query, "烤串", "串");
            case "现炸薯条" -> containsAny(query, "薯条");
            case "鲜榨果汁" -> containsAny(query, "果汁", "饮品");
            default -> false;
        };
    }

    private static int toInt(Object value, int fallback) {
        try {
            if (value instanceof Number number) {
                return number.intValue();
            }
            return (int) Double.parseDouble(String.valueOf(value));
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
