-- 为待审核演示摊位补齐真实商品，保证后台审批释放后用户端可浏览、可下单。
SET @vendor_old_town = (SELECT id FROM t_vendor WHERE vendor_name = '老城豆花摊' LIMIT 1);
SET @stall_old_town = (SELECT id FROM t_stall WHERE stall_name = '老城豆花摊' LIMIT 1);
SET @cat_food = (SELECT id FROM t_product_category WHERE category_name = '小吃' LIMIT 1);
SET @cat_drink = (SELECT id FROM t_product_category WHERE category_name = '饮品' LIMIT 1);

INSERT INTO t_product(vendor_id, stall_id, category_id, product_name, price, status, description) VALUES
(@vendor_old_town, @stall_old_town, @cat_food, '老城手作豆花', 8.00, 'on_sale', '传统石磨豆花，口感细滑。'),
(@vendor_old_town, @stall_old_town, @cat_food, '咸口豆腐脑', 9.00, 'on_sale', '卤汁现浇，适合晚餐自提。'),
(@vendor_old_town, @stall_old_town, @cat_drink, '桂花冰豆浆', 6.00, 'on_sale', '低糖豆浆，带桂花清香。');
