# 部署说明

## 本地依赖

```bash
cd deploy
docker compose up -d mysql redis
```

MySQL 宿主机端口为 `13307`，容器内端口仍为 `3306`。本地后端通过 `MYSQL_PORT=13307` 连接。

## 一键启动

```powershell
./deploy/scripts/start-dev.ps1
```

脚本会启动 MySQL、Redis、后端、H5 前台和管理后台，并把日志写入 `deploy/logs`。

Redis 无需额外初始化。当前框架阶段只是接入连接配置，后续验证码、缓存、限流、通知等能力会逐步使用它。

停止：

```powershell
./deploy/scripts/stop-dev.ps1
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
