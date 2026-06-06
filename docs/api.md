# API 规范

统一前缀：`/api`

统一响应：

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": "2026-06-06T12:00:00"
}
```

核心接口已在后端 Controller 中创建骨架，Swagger 地址：

```text
http://localhost:8080/swagger-ui/index.html
```

分页格式：

```json
{
  "total": 1,
  "pageNo": 1,
  "pageSize": 10,
  "records": []
}
```
