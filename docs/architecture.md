# 架构说明

云摊坊采用前后端分离架构：

- H5 前台：消费者与摊主共用响应式移动优先站点。
- Admin 后台：平台运营、审核、监管和系统管理员使用的独立后台。
- Backend：统一 Spring Boot REST API，按业务模块组织。
- Database：MySQL 8 存储核心业务数据，Flyway 管理结构迁移。
- Redis：缓存、验证码、限流与会话辅助能力预留。
- Integration：地图、支付、短信、对象存储、通知、内容安全均通过接口适配层接入，开发阶段使用 mock。

后端包结构以 `com.yuntanfang.module` 为业务边界，`common`、`config`、`security` 提供横向能力。
