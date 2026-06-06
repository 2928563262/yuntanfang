# 部署说明

## 本地依赖

```bash
cd deploy
docker compose up -d mysql redis
```

## 后端

```bash
cd backend
mvn spring-boot:run
```

## 前端

```bash
cd frontend
pnpm install
pnpm dev:h5
pnpm dev:admin
```

## Nginx

示例配置位于 `deploy/nginx/yuntanfang.conf`，生产环境需要替换域名、证书、静态资源路径和 upstream。
