-- 订单增加可展示字段（金额、取货时间、联系方式、摊位快照），订单项增加商品名/单价快照。
ALTER TABLE t_order
  ADD COLUMN stall_id BIGINT NULL,
  ADD COLUMN stall_name VARCHAR(128) NULL,
  ADD COLUMN total_amount DECIMAL(10, 2) NULL,
  ADD COLUMN pickup_time VARCHAR(64) NULL,
  ADD COLUMN contact_phone VARCHAR(32) NULL,
  ADD COLUMN remark VARCHAR(255) NULL;

ALTER TABLE t_order_item
  ADD COLUMN product_name VARCHAR(128) NULL,
  ADD COLUMN price DECIMAL(10, 2) NULL;

-- 给 V3 灌入的演示订单补展示字段
UPDATE t_order SET stall_id = 1, stall_name = '烟火小摊', total_amount = 59.80,
  pickup_time = '今天 18:30', contact_phone = '13000000001'
  WHERE stall_name IS NULL;

UPDATE t_order_item SET product_name = '招牌烤串(10串)', price = 29.90 WHERE product_name IS NULL;
