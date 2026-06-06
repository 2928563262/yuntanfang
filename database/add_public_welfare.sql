USE yun_tan_fang;

CREATE TABLE IF NOT EXISTS t_public_welfare_tag (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tag_code VARCHAR(64) NOT NULL UNIQUE,
  tag_name VARCHAR(64) NOT NULL,
  description VARCHAR(512),
  status VARCHAR(32) NOT NULL DEFAULT 'enabled',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE t_special_identity
  ADD COLUMN public_welfare_tag_id BIGINT NULL,
  ADD COLUMN auditor_id BIGINT NULL,
  ADD COLUMN audit_opinion VARCHAR(512) NULL,
  ADD COLUMN reject_reason VARCHAR(512) NULL,
  ADD COLUMN display_on_front TINYINT NOT NULL DEFAULT 0;

INSERT IGNORE INTO t_public_welfare_tag(tag_code, tag_name, description) VALUES
('craft_guardian', '守艺匠人', '非遗传承与传统手艺公益展示标签'),
('veteran_creator', '戎创达人', '退伍军人创业公益展示标签'),
('youth_creator', '青春创客', '大学生创业公益展示标签'),
('rural_farmer', '乡野新农人', '助农农户公益展示标签'),
('light_entrepreneur', '微光创业者', '残疾人、低保户等帮扶公益展示标签');
