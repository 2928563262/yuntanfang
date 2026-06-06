# 云摊坊 · 智慧摊务一体化管理系统

云摊坊是面向消费者、摊主、平台运营与监管人员的智慧摊务一体化管理平台。当前仓库是前后端分离 monorepo 项目骨架，包含 Spring Boot 后端、Vue 3 H5 前台、Vue 3 管理后台、数据库迁移、Docker Compose、CI 与协作文档。

## 技术栈

- 后端：Java 17、Spring Boot 3、Maven、Spring Web、Spring Validation、Spring Security、MyBatis-Plus、Flyway、Redis、springdoc-openapi、Lombok、JUnit 5
- 前端：Vue 3、Vite、TypeScript、Pinia、Vue Router、Axios、Vant、Element Plus、ECharts 预留、ESLint、Prettier、pnpm workspace
- 基础设施：MySQL 8、Redis、Docker Compose、Nginx 示例配置、GitHub Actions

## 目录结构

```text
backend/        Spring Boot REST API
frontend/       pnpm workspace 前端 monorepo
database/       原始 SQL 与数据库说明
docs/           架构、接口、数据库、部署、协作文档
deploy/         Docker Compose、Nginx、脚本
.github/        CI、Issue 与 PR 模板
```

## 本地启动

```bash
# 启动基础服务
cd deploy
docker compose up -d mysql redis

# 一条命令启动依赖、后端、H5 和管理后台
cd ..
./deploy/scripts/start-dev.ps1

# 启动后端
cd backend
mvn spring-boot:run

# 启动前端
cd ../frontend
pnpm install
pnpm dev:h5
pnpm dev:admin
```

## 环境变量

复制 `.env.example` 为本地 `.env`，按需配置数据库、Redis、JWT、第三方 mock 开关等变量。不要提交真实密钥。

## 数据库初始化

开发环境默认由 Flyway 执行：

- `backend/src/main/resources/db/migration/V1__init_schema.sql`
- `backend/src/main/resources/db/migration/V2__public_welfare_tag.sql`

也可以直接查看 `database/create_database.sql` 与 `database/add_public_welfare.sql`。

## 访问地址

- 后端 API：`http://localhost:8080/api`
- Swagger UI：`http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON：`http://localhost:8080/v3/api-docs`
- H5 前台：`http://localhost:5173`
- 管理后台：`http://localhost:5174`

## 统一启动脚本

Windows PowerShell：

```powershell
./deploy/scripts/start-dev.ps1
```

停止本地开发进程：

```powershell
./deploy/scripts/stop-dev.ps1
```

脚本会使用 Docker Compose 启动 MySQL 和 Redis。MySQL 宿主机端口默认是 `13307`，避免和本机已有 MySQL 冲突；Redis 使用 `6379`。

Redis 在当前阶段只作为缓存、验证码、限流等能力预留。开发时不需要手工创建库或表，保持 Docker 容器运行即可；后端通过 `REDIS_HOST`、`REDIS_PORT` 自动连接。

## Git 规范

分支：

- `main`：稳定主分支，保护分支
- `dev`：日常集成分支
- `feature/<module-name>`：功能开发分支
- `fix/<issue-name>`：缺陷修复分支
- `chore/<task-name>`：工程配置或文档分支
- `release/<version>`：发布准备分支

提交使用 Conventional Commits，例如：

```bash
git checkout -b feature/vendor-apply
git add .
git commit -m "feat: initialize vendor application module scaffold"
```

## 团队开发流程

1. 从 `dev` 创建功能分支。
2. 本地完成开发、测试与格式化。
3. 提交 PR，关联 Issue，等待 CI 通过。
4. 至少一名同伴 Review 后合并。
5. 发布前从 `dev` 创建 `release/<version>`，验证后合并到 `main` 并打 tag。
