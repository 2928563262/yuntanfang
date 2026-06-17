# create_order

## 功能

把用户自然语言点单转换为预约订单草稿。

## 触发语

- "帮我订两份招牌烤串"
- "预约烟火小摊 18:30 取"
- "我要买现炸薯条"

## 参数

```json
{
  "stallName": "烟火小摊",
  "productName": "招牌烤串(10串)",
  "quantity": 2,
  "pickupTime": "今天 18:30",
  "contact": ""
}
```

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `stallName` | string | 否 | 摊位名 |
| `productName` | string | 必填 | 商品名，必须来自用户明确表达 |
| `quantity` | number | 否 | 数量，缺省 1 |
| `pickupTime` | string | 否 | 取货时间，缺省 `今天 19:00` |
| `contact` | string | 否 | 联系方式，后续正式下单时补充 |

`productName` 必须明确出现，且必须能匹配真实 `t_product.status = on_sale` 商品。只说“我要预约晚餐”“我想预约”“预约这个摊位”时必须先追问，不能按摊位默认创建订单。`stallName`、`vendorId`、`stallId`、`price` 由后端根据真实商品和摊位表反查，不由模型猜。

系统默认值：

- `quantity`: 用户未说数量时后端使用 `1`。
- `pickupTime`: 用户未说取货时间时后端使用 `今天 19:00`。
- `stallName`/`vendorId`/`stallId`/`price`: 用户明确商品但未说摊位时，后端按真实商品和摊位表反查。

禁止行为：

- 用户只说“我想预约”，不要默认生成“招牌烤串”订单。
- 用户只说“预约烟火小摊”，不要默认生成该摊位招牌商品。
- 用户纠正“我没说我要烤串”时，应清空当前订单草稿并追问具体商品。
- 不要从“晚餐”“吃的”“来一份”推断具体商品。
- 不要输出数据库不存在或未上架的商品。

## 参数不足响应

追问文案：

`你想预约哪个商品？目前可预约招牌烤串(10串)、现炸薯条、鲜榨果汁。请先告诉我具体商品，再帮你生成订单。`

## 响应 action

```json
{
  "type": "create_order",
  "label": "确认创建订单",
  "route": "/orders",
  "payload": {
    "vendorId": 1,
    "stallId": 1,
    "productId": 1,
    "stallName": "烟火小摊",
    "productName": "招牌烤串(10串)",
    "quantity": 2,
    "pickupTime": "今天 18:30",
    "price": "29.90",
    "totalAmount": "59.80",
    "status": "待接单",
    "orderPayload": {
      "vendorId": 1,
      "stallId": 1,
      "stallName": "烟火小摊",
      "pickupTime": "今天 18:30",
      "contactPhone": "",
      "remark": "Agent 创建订单草稿",
      "items": [
        {
          "productId": 1,
          "productName": "招牌烤串(10串)",
          "quantity": 2,
          "price": "29.90"
        }
      ]
    }
  }
}
```

## 前端行为

用户确认后调用 `POST /api/orders`，请求体使用 `action.payload.orderPayload`，成功后跳转 `/orders/:id`。该订单由数据库保存，因此普通用户 `/orders/my` 和商家 `/vendor/orders` 会看到同一条订单。
