# system_help

## 功能

回答用户关于系统能力、页面入口、使用流程的问题。

## 触发语

- "这个系统能做什么"
- "订单在哪看"
- "怎么投诉"
- "收藏入口在哪里"

## 参数

```json
{
  "topic": "订单怎么评价"
}
```

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `topic` | string | 否 | 用户想了解的功能或入口 |

## 边界

- 只回答功能入口、使用流程、系统能做什么。
- 用户表达明确业务动作时，应切到对应 intent，不要用 `system_help` 绕过参数校验。
- 用户问“怎么评价订单”是 `system_help`；用户说“给上一单好评”是 `submit_review`。
- 用户问“怎么投诉”是 `system_help`；用户说“投诉烟火小摊卫生问题”是 `submit_complaint`。

## 响应 action

```json
{
  "type": "open_route",
  "label": "打开相关入口",
  "route": "/orders",
  "payload": {
    "topic": "订单怎么评价",
    "route": "/orders"
  }
}
```

## 前端行为

展示说明文案，并允许用户一键跳转相关页面。
