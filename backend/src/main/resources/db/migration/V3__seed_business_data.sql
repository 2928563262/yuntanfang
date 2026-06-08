-- 演示业务数据：让真实接口返回非空结果（关联 create_database.sql 灌入的 test1/test2 账号）。
SET @vendor_id = (SELECT id FROM t_vendor WHERE user_id = (SELECT id FROM t_user WHERE username = 'test2') LIMIT 1);
SET @user1_id = (SELECT id FROM t_user WHERE username = 'test1' LIMIT 1);

INSERT INTO t_area(area_name, status) VALUES
('城南夜市', 'enabled'),
('文创园周末集市', 'enabled');
SET @area_id = (SELECT id FROM t_area ORDER BY id LIMIT 1);

INSERT INTO t_stall(vendor_id, area_id, stall_name, business_status) VALUES
(@vendor_id, @area_id, '烟火小摊', 'open'),
(@vendor_id, @area_id, '乡野新农人鲜铺', 'open'),
(NULL, @area_id, '非遗手作摊', 'closed');

INSERT INTO t_product_category(category_name, status) VALUES
('小吃', 'enabled'),
('饮品', 'enabled'),
('农产品', 'enabled');
SET @cat_id = (SELECT id FROM t_product_category ORDER BY id LIMIT 1);

INSERT INTO t_product(vendor_id, category_id, product_name, price, status) VALUES
(@vendor_id, @cat_id, '招牌烤串(10串)', 29.90, 'on_sale'),
(@vendor_id, @cat_id, '现炸薯条', 12.00, 'on_sale'),
(@vendor_id, @cat_id, '鲜榨果汁', 15.00, 'on_sale');
SET @prod_id = (SELECT id FROM t_product ORDER BY id LIMIT 1);

INSERT INTO t_order(user_id, vendor_id, order_status) VALUES
(@user1_id, @vendor_id, 'created'),
(@user1_id, @vendor_id, 'completed');
SET @order_id = (SELECT id FROM t_order ORDER BY id DESC LIMIT 1);

INSERT INTO t_order_item(order_id, product_id, quantity) VALUES
(@order_id, @prod_id, 2);

INSERT INTO t_order_status_log(order_id, order_status) VALUES
(@order_id, 'created'),
(@order_id, 'completed');

INSERT INTO t_review(order_id, user_id, rating, status) VALUES
(@order_id, @user1_id, 5, 'published');

INSERT INTO t_complaint(user_id, vendor_id, description, status) VALUES
(@user1_id, @vendor_id, '排队时间较长，希望增加取餐窗口。', 'submitted');

INSERT INTO t_stall_reservation(vendor_id, stall_id, status)
SELECT @vendor_id, id, 'pending' FROM t_stall WHERE stall_name = '非遗手作摊' LIMIT 1;

INSERT INTO t_policy(title, content, status) VALUES
('夜市经营管理规范', '摊主需亮证经营，按区域定点摆摊，垃圾分类投放。', 'published');

INSERT INTO t_notice(title, content, status) VALUES
('端午主题集市活动通知', '6 月将举办端午主题集市，欢迎符合条件的摊主报名参与。', 'published');
