# create_order

## 功能

把用户自然语言点单转换为预约订单草稿。

## 触发语

- "帮我订两份招牌汤粉"
- "预约烟火小摊 18:30 取"
- "我要买农家土鸡蛋"

## 参数

```json
{
  "stallName": "烟火小摊",
  "productName": "招牌汤粉",
  "quantity": 2,
  "pickupTime": "今天 18:30",
  "contact": ""
}
```

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `stallName` | string | 否 | 摊位名，缺省为推荐摊位 |
| `productName` | string | 否 | 商品名，缺省为该摊位招牌商品 |
| `quantity` | number | 否 | 数量，缺省 1 |
| `pickupTime` | string | 否 | 取货时间，缺省 `今天 19:00` |
| `contact` | string | 否 | 联系方式，后续正式下单时补充 |

## 响应 action

```json
{
  "type": "create_order",
  "label": "确认创建订单",
  "route": "/orders",
  "payload": {
    "orderId": 185631,
    "stallName": "烟火小摊",
    "productName": "招牌汤粉",
    "quantity": 2,
    "pickupTime": "今天 18:30",
    "totalAmount": "32.00",
    "status": "待支付"
  }
}
```

## 前端行为

用户确认后写入本地订单 store，并跳转 `/orders/:id`。
