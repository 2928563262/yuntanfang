CREATE TABLE IF NOT EXISTS t_public_welfare_tag (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tag_code VARCHAR(64) NOT NULL UNIQUE,
  tag_name VARCHAR(64) NOT NULL,
  description VARCHAR(512),
  status VARCHAR(32) NOT NULL DEFAULT 'enabled',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DELIMITER //

CREATE PROCEDURE add_column_if_missing(
  IN table_name_value VARCHAR(64),
  IN column_name_value VARCHAR(64),
  IN column_definition_value VARCHAR(512)
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = table_name_value
      AND column_name = column_name_value
  ) THEN
    SET @ddl = CONCAT('ALTER TABLE ', table_name_value, ' ADD COLUMN ', column_name_value, ' ', column_definition_value);
    PREPARE stmt FROM @ddl;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END //

DELIMITER ;

CALL add_column_if_missing('t_special_identity', 'public_welfare_tag_id', 'BIGINT NULL');
CALL add_column_if_missing('t_special_identity', 'auditor_id', 'BIGINT NULL');
CALL add_column_if_missing('t_special_identity', 'audit_opinion', 'VARCHAR(512) NULL');
CALL add_column_if_missing('t_special_identity', 'reject_reason', 'VARCHAR(512) NULL');
CALL add_column_if_missing('t_special_identity', 'display_on_front', 'TINYINT NOT NULL DEFAULT 0');

DROP PROCEDURE add_column_if_missing;

INSERT IGNORE INTO t_public_welfare_tag(tag_code, tag_name, description) VALUES
('craft_guardian', '守艺匠人', '非遗传承与传统手艺公益展示标签'),
('veteran_creator', '戎创达人', '退伍军人创业公益展示标签'),
('youth_creator', '青春创客', '大学生创业公益展示标签'),
('rural_farmer', '乡野新农人', '助农农户公益展示标签'),
('light_entrepreneur', '微光创业者', '残疾人、低保户等帮扶公益展示标签');
