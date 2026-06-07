# submit_review

## 功能

帮助用户根据订单提交评价。

## 触发语

- "给上一单好评"
- "帮我评价订单 1002，服务很好"
- "这单体验一般，给 3 星"

## 参数

```json
{
  "orderId": 1002,
  "rating": 5,
  "content": "服务很好，取餐很快。"
}
```

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `orderId` | number | 否 | 订单 ID，缺省使用最近可评价订单 |
| `rating` | number | 否 | 1-5 星，缺省 5 |
| `content` | string | 否 | 评价内容 |

## 响应 action

```json
{
  "type": "submit_review",
  "label": "确认发布评价",
  "route": "/my-reviews",
  "payload": {
    "reviewId": 185702,
    "orderId": 1002,
    "rating": 5,
    "content": "服务很好，取餐很快。",
    "status": "已发布"
  }
}
```

## 前端行为

用户确认后写入评价 store，并跳转 `/my-reviews`。
