# Git 工作流

## 分支

- `main`：稳定主分支，保护分支
- `dev`：日常集成分支
- `feature/<module-name>`：功能开发分支，例如 `feature/vendor-apply`
- `fix/<issue-name>`：缺陷修复分支
- `chore/<task-name>`：工程配置或文档分支
- `release/<version>`：发布准备分支

## Commit

使用 Conventional Commits：

- `feat`: 新功能
- `fix`: 修复问题
- `docs`: 文档
- `style`: 格式调整，不改变逻辑
- `refactor`: 重构
- `test`: 测试
- `chore`: 构建、依赖、工程配置
- `ci`: CI/CD 配置

首个初始化提交：

```bash
git add .
git commit -m "chore: initialize yuntanfang monorepo scaffold"
```

不提供 GitHub remote URL 时，不推送远程仓库。
