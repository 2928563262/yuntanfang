# 数据库说明

数据库名：`yun_tan_fang`

当前 SQL 为框架阶段的基础骨架，保留核心表清单、`t_` 前缀、`created_at`、`updated_at`、角色编码与 `t_supervisor` 命名。若已有正式建库脚本，请替换：

- `database/create_database.sql`
- `database/add_public_welfare.sql`
- `backend/src/main/resources/db/migration/V1__init_schema.sql`
- `backend/src/main/resources/db/migration/V2__public_welfare_tag.sql`
