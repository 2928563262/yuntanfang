# 贡献指南

## 分支规范

- `main`：稳定主分支，保护分支
- `dev`：日常集成分支
- `feature/<module-name>`：功能开发分支，例如 `feature/vendor-apply`
- `fix/<issue-name>`：缺陷修复分支
- `chore/<task-name>`：工程配置或文档分支
- `release/<version>`：发布准备分支

## Commit 规范

使用 Conventional Commits：

- `feat`: 新功能
- `fix`: 修复问题
- `docs`: 文档
- `style`: 格式调整，不改变逻辑
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建、依赖、工程配置
- `ci`: CI/CD 配置

示例：

```bash
git checkout -b feature/vendor-apply
git add .
git commit -m "feat: initialize vendor application module scaffold"
```

## PR 要求

- 说明业务背景、改动范围和验证方式。
- 不提交真实密钥、本地 `.env`、构建产物和 IDE 私有配置。
- 后端接口变更同步更新 Swagger 注解或 `docs/api.md`。
- 数据库结构变更必须新增 Flyway migration。
