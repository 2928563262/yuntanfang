---
name: yuntanfang-agent
description: 云摊坊系统 Agent 总 skill。用于理解用户自然语言请求，选择可执行功能 API，返回可确认的前端 action，并提供系统功能导航帮助。
---

# 云摊坊 Agent Skill

## 目标

Agent 帮助普通用户更快掌握和使用云摊坊：

- 直接执行操作：找摊位、创建预约订单、提交评价、提交投诉。
- 辅助使用系统：解释功能入口、说明可以做什么、引导用户到正确页面。
- 保持可审计：模型只做意图识别和参数抽取，后端只执行白名单 action。

## 当前统一入口

`POST /api/agent/chat`

请求：

```json
{
  "message": "帮我找附近的糖画摊",
  "history": ["assistant: 你可以问我入口在哪"],
  "context": {
    "currentRoute": "/agent-order",
    "role": "consumer"
  }
}
```

响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "reply": "已帮你找到 1 个相关摊位，可以点卡片查看详情或继续让我筛选。",
    "intent": "search_stalls",
    "action": {
      "type": "open_route",
      "label": "查看摊位列表",
      "route": "/stalls",
      "payload": {
        "keyword": "糖画",
        "category": "非遗好物"
      }
    },
    "cards": [],
    "suggestedPrompts": ["帮我找附近摊位", "帮我预约一份晚餐"],
    "status": "deepseek",
    "rawModelOutput": "{}"
  }
}
```

## 白名单意图

| Intent | 小 skill | 作用 |
| --- | --- | --- |
| `search_stalls` | `skills/search-stalls.md` | 搜索摊位、分类筛选、推荐附近摊位 |
| `create_order` | `skills/create-order.md` | 从自然语言生成预约订单草稿 |
| `submit_review` | `skills/submit-review.md` | 根据订单和内容提交评价 |
| `submit_complaint` | `skills/submit-complaint.md` | 生成投诉工单 |
| `system_help` | `skills/system-help.md` | 解释系统能力和功能入口 |

## 后端执行规则

1. 使用 DeepSeek 只输出 JSON 决策：

```json
{
  "intent": "create_order",
  "parameters": {
    "stallName": "烟火小摊",
    "productName": "招牌汤粉",
    "quantity": 2,
    "pickupTime": "今天 18:30",
    "contact": ""
  }
}
```

2. 后端校验 intent，只接受白名单。
3. 后端补默认值并生成 action。
4. 前端只执行后端返回的 action，不执行模型原始文本。
5. DeepSeek 不可用或未配置 key 时，后端走本地规则降级。

## 前端执行规则

- `open_route`：跳转到 `action.route`。
- `create_order`：用 `action.payload` 写入 H5 本地订单 store，再跳转订单详情。
- `submit_review`：写入 H5 本地评价 store，再跳转 `/my-reviews`。
- `submit_complaint`：写入 H5 本地投诉 store，再跳转 `/complaints`。

## 待开发

见 `TODO.md`。
