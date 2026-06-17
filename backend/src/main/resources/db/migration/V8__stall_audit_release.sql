-- 打通"商家入驻→后台审核→摊位释放到用户端"链路。
-- 摊位审核状态(audit_status)与营业状态(business_status)区分：
--   audit_status: pending/approved/rejected —— 仅 approved 的摊位对用户端可展示、可搜索、可预约。
--   business_status: open/closed —— 摊主日常出摊/休息开关。

ALTER TABLE t_stall ADD COLUMN audit_status VARCHAR(32) NOT NULL DEFAULT 'pending';

-- 审核追溯字段（意见 / 驳回原因）
ALTER TABLE t_vendor ADD COLUMN audit_opinion VARCHAR(512) NULL;
ALTER TABLE t_vendor ADD COLUMN reject_reason VARCHAR(512) NULL;
ALTER TABLE t_qualification ADD COLUMN audit_opinion VARCHAR(512) NULL;
ALTER TABLE t_qualification ADD COLUMN reject_reason VARCHAR(512) NULL;
ALTER TABLE t_stall_reservation ADD COLUMN reject_reason VARCHAR(512) NULL;

-- 既有营业中的摊位（V3/V5 灌入）保持对用户端可见，避免破坏现有演示
UPDATE t_stall SET audit_status = 'approved' WHERE business_status = 'open';
-- 非遗手作摊保持 pending（有一条待审批预约），用于演示"审批通过后释放"

-- 现有摊主(test2)：补一条待审核资质与一条待审核公益标签，撑起审核队列演示
SET @vendor2 = (SELECT id FROM t_vendor WHERE user_id = (SELECT id FROM t_user WHERE username = 'test2' LIMIT 1) LIMIT 1);
UPDATE t_vendor SET status = 'approved' WHERE id = @vendor2 AND status <> 'rejected';
INSERT INTO t_qualification(vendor_id, qualification_type, media_url, status) VALUES
(@vendor2, 'health_certificate', 'https://example.com/demo/health-test2.jpg', 'pending');
SET @welfare_craft = (SELECT id FROM t_public_welfare_tag WHERE tag_code = 'craft_guardian' LIMIT 1);
INSERT INTO t_special_identity(vendor_id, identity_type, public_welfare_tag_id, status, display_on_front) VALUES
(@vendor2, 'public_welfare', @welfare_craft, 'pending', 0);

-- 全新待审核摊主：演示完整"入驻申请→资质/公益认证→摊位预约→后台四级审核→用户端释放"链路
-- 注意：用户名不能用 test3（create_database.sql 中 test3 已是后台管理员账号，会与 t_admin 撞名导致登录走错分支）
INSERT INTO t_user(username, password_hash, mobile, status, account_type) VALUES ('test4', '123456', '13900000004', 'active', 'vendor');
SET @user3 = (SELECT id FROM t_user WHERE username = 'test4' LIMIT 1);
INSERT INTO t_vendor(user_id, vendor_name, story, status) VALUES
(@user3, '老城豆花摊', '三代相传的手作豆花，想在夜市与街坊们重逢。', 'pending');
SET @vendor3 = (SELECT id FROM t_vendor WHERE user_id = @user3 LIMIT 1);
INSERT INTO t_qualification(vendor_id, qualification_type, media_url, status) VALUES
(@vendor3, 'health_certificate', 'https://example.com/demo/health-test3.jpg', 'pending'),
(@vendor3, 'stall_photo', 'https://example.com/demo/stall-test3.jpg', 'pending');
SET @welfare_youth = (SELECT id FROM t_public_welfare_tag WHERE tag_code = 'youth_creator' LIMIT 1);
INSERT INTO t_special_identity(vendor_id, identity_type, public_welfare_tag_id, status, display_on_front) VALUES
(@vendor3, 'public_welfare', @welfare_youth, 'pending', 0);

SET @area_id = (SELECT id FROM t_area ORDER BY id LIMIT 1);
INSERT INTO t_stall(vendor_id, area_id, stall_name, business_status, audit_status, category, rating, address, distance, description, vendor_name) VALUES
(NULL, @area_id, '老城豆花摊', 'closed', 'pending', '传统小吃', 4.6, '老城十字街临时摊区', '900m', '手作豆花、豆腐脑，主打怀旧口味，支持预约自提。', '老城豆花摊');
SET @stall3 = (SELECT id FROM t_stall WHERE stall_name = '老城豆花摊' LIMIT 1);
INSERT INTO t_stall_reservation(vendor_id, stall_id, status) VALUES (@vendor3, @stall3, 'pending');
