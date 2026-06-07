# search_stalls

## 功能

帮助用户查找摊位，可按关键词、分类、商品、位置进行筛选。

## 触发语

- "附近有什么摊位"
- "找糖画摊"
- "有没有农家特产"
- "推荐评分高的摊位"

## 参数

```json
{
  "keyword": "糖画",
  "category": "非遗好物"
}
```

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `keyword` | string | 否 | 用户搜索词，可包含摊位、商品、位置 |
| `category` | string | 否 | 分类，如 `地方特色`、`农家特产`、`非遗好物` |

## 响应 action

```json
{
  "type": "open_route",
  "label": "查看摊位列表",
  "route": "/stalls",
  "payload": {
    "keyword": "糖画",
    "category": "非遗好物"
  }
}
```

## 前端行为

展示摊位卡片，用户确认后跳转 `/stalls`。
