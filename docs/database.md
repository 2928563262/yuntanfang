# 数据库设计说明

数据库：`yun_tan_fang`

命名原则：

- 表名使用 `t_` 前缀。
- 字段使用英文小写加下划线。
- 主键优先 BIGINT 自增。
- 核心表保留 `created_at`、`updated_at`。
- 角色编码使用 `role_code`。
- 监管人员表使用 `t_supervisor`。
- 微信相关字段只保留可选联系方式 `wechat_id`。

迁移文件：

- `backend/src/main/resources/db/migration/V1__init_schema.sql`
- `backend/src/main/resources/db/migration/V2__public_welfare_tag.sql`
