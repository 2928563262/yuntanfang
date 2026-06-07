# submit_complaint

## 功能

帮助用户把自然语言投诉整理为投诉工单。

## 触发语

- "我要投诉烟火小摊"
- "摊位卫生有问题"
- "有人占道经营"

## 参数

```json
{
  "target": "烟火小摊",
  "type": "卫生问题",
  "description": "现场卫生需要核实。"
}
```

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `target` | string | 否 | 投诉对象，摊位、摊区或订单 |
| `type` | string | 否 | 问题类型，如卫生问题、占道经营、商品质量、服务问题 |
| `description` | string | 否 | 详细描述 |

## 响应 action

```json
{
  "type": "submit_complaint",
  "label": "确认提交投诉",
  "route": "/complaints",
  "payload": {
    "complaintId": 185733,
    "target": "烟火小摊",
    "type": "卫生问题",
    "description": "现场卫生需要核实。",
    "status": "处理中"
  }
}
```

## 前端行为

用户确认后写入投诉 store，并跳转 `/complaints`。
