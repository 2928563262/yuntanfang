-- 给展示相关表补字段，并灌入与前端原 mock 等量的丰富数据。
ALTER TABLE t_stall
  ADD COLUMN category VARCHAR(64) NULL,
  ADD COLUMN rating DECIMAL(2, 1) NULL,
  ADD COLUMN address VARCHAR(255) NULL,
  ADD COLUMN distance VARCHAR(32) NULL,
  ADD COLUMN description VARCHAR(512) NULL,
  ADD COLUMN cover_image VARCHAR(512) NULL,
  ADD COLUMN vendor_name VARCHAR(128) NULL;

ALTER TABLE t_product
  ADD COLUMN description VARCHAR(255) NULL,
  ADD COLUMN image VARCHAR(512) NULL;

ALTER TABLE t_review
  ADD COLUMN content VARCHAR(512) NULL,
  ADD COLUMN user_name VARCHAR(64) NULL,
  ADD COLUMN stall_id BIGINT NULL,
  ADD COLUMN stall_name VARCHAR(128) NULL;

ALTER TABLE t_favorite
  ADD COLUMN biz_name VARCHAR(128) NULL;

ALTER TABLE t_subscribe
  ADD COLUMN vendor_name VARCHAR(128) NULL;

-- 丰富已有摊位（V3 已插入三条）
UPDATE t_stall SET category='地方特色', rating=4.8, address='北站中心公园东门', distance='680m',
  description='主打现做小吃和本地特色汤粉，支持预约取餐。', vendor_name='林师傅', business_status='open'
  WHERE stall_name='烟火小摊';
UPDATE t_stall SET category='农家特产', rating=4.7, address='市民广场南侧临时摊区', distance='1.2km',
  description='助农农户直供，售卖当季蔬果、土鸡蛋和手工酱菜。', vendor_name='陈小禾', business_status='open'
  WHERE stall_name='乡野新农人鲜铺';
UPDATE t_stall SET category='非遗好物', rating=4.9, address='老街口文创夜市', distance='2.0km',
  description='非遗糖画体验摊，适合亲子互动和城市文化打卡。', vendor_name='周老师', business_status='closed'
  WHERE stall_name='非遗手作摊';

-- 商品补描述
UPDATE t_product SET description='现做现卖，支持预约取货' WHERE description IS NULL;

-- 评价（绑定到烟火小摊，便于摊位详情展示）
SET @stall1 = (SELECT id FROM t_stall WHERE stall_name='烟火小摊' LIMIT 1);
SET @stall2 = (SELECT id FROM t_stall WHERE stall_name='乡野新农人鲜铺' LIMIT 1);
SET @order1 = (SELECT id FROM t_order ORDER BY id LIMIT 1);
INSERT INTO t_review(order_id, user_id, rating, status, content, user_name, stall_id, stall_name) VALUES
(@order1, 1, 5, 'published', '汤粉很香，分量足，取餐很快！', '热心市民', @stall1, '烟火小摊'),
(@order1, 1, 4, 'published', '味道不错，就是高峰期排队稍久。', '吃货小王', @stall1, '烟火小摊'),
(@order1, 1, 5, 'published', '蔬果很新鲜，助农很有意义。', '本地阿姨', @stall2, '乡野新农人鲜铺');

-- 已有评价补摊位归属，避免详情页查不到
UPDATE t_review SET stall_id=@stall1, stall_name='烟火小摊', content=COALESCE(content,'体验不错'), user_name=COALESCE(user_name,'匿名用户')
  WHERE stall_id IS NULL;
