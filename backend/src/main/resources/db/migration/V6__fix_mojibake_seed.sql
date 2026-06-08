-- 修复 create_database.sql 经 Docker 初始化导入时产生的双重编码乱码。
-- Flyway 走 JDBC(characterEncoding=utf8），UPDATE 会以正确 UTF-8 写回。
UPDATE t_role SET role_name = '消费者' WHERE role_code = 'consumer';
UPDATE t_role SET role_name = '摊主' WHERE role_code = 'vendor';
UPDATE t_role SET role_name = '管理人员' WHERE role_code = 'admin';
UPDATE t_role SET role_name = '审核人员' WHERE role_code = 'auditor';
UPDATE t_role SET role_name = '监管人员' WHERE role_code = 'supervisor';
UPDATE t_role SET role_name = '系统管理员' WHERE role_code = 'system_admin';

UPDATE t_vendor
SET vendor_name = 'test2 的示例摊位',
    story = '用于商家端前端开发的测试摊主账号。'
WHERE user_id = (SELECT id FROM t_user WHERE username = 'test2');
