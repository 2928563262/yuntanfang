-- 商品绑定到具体摊位，避免同一摊主名下多个摊位展示完全相同的商品。
ALTER TABLE t_product
  ADD COLUMN stall_id BIGINT NULL AFTER vendor_id;

SET @vendor_id = (SELECT id FROM t_vendor WHERE user_id = (SELECT id FROM t_user WHERE username = 'test2') LIMIT 1);
SET @cat_food = (SELECT id FROM t_product_category WHERE category_name = '小吃' LIMIT 1);
SET @cat_drink = (SELECT id FROM t_product_category WHERE category_name = '饮品' LIMIT 1);
SET @cat_farm = (SELECT id FROM t_product_category WHERE category_name = '农产品' LIMIT 1);
SET @stall_fire = (SELECT id FROM t_stall WHERE stall_name = '烟火小摊' LIMIT 1);
SET @stall_farm = (SELECT id FROM t_stall WHERE stall_name = '乡野新农人鲜铺' LIMIT 1);
SET @stall_craft = (SELECT id FROM t_stall WHERE stall_name = '非遗手作摊' LIMIT 1);

UPDATE t_stall
SET vendor_id = @vendor_id
WHERE stall_name = '非遗手作摊' AND vendor_id IS NULL;

UPDATE t_product
SET stall_id = @stall_fire
WHERE product_name IN ('招牌烤串(10串)', '现炸薯条', '鲜榨果汁');

INSERT INTO t_product(vendor_id, stall_id, category_id, product_name, price, status, description) VALUES
(@vendor_id, @stall_farm, @cat_farm, '当季蔬果礼盒', 26.80, 'on_sale', '农户直供，当天分拣。'),
(@vendor_id, @stall_farm, @cat_farm, '农家土鸡蛋(10枚)', 18.00, 'on_sale', '散养土鸡蛋，适合自提。'),
(@vendor_id, @stall_farm, @cat_farm, '手工辣酱', 16.00, 'on_sale', '手工熬制，下饭拌面皆可。'),
(@vendor_id, @stall_craft, @cat_food, '生肖糖画', 12.00, 'on_sale', '现场制作，可选生肖图案。'),
(@vendor_id, @stall_craft, @cat_food, '定制糖牌', 15.00, 'on_sale', '支持文字定制。'),
(@vendor_id, @stall_craft, @cat_food, '糖画体验券', 20.00, 'on_sale', '亲子体验项目。');
