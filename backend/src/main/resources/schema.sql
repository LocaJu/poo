-- SubletHub 数据库建表脚本（首次部署执行）
-- 使用前创建数据库：CREATE DATABASE sublethub DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE sublethub;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  open_id VARCHAR(64) NOT NULL,
  nick_name VARCHAR(64),
  avatar_url VARCHAR(512),
  phone VARCHAR(20),
  wechat_id VARCHAR(64),
  is_verified TINYINT(1) DEFAULT 0,
  id_card_front VARCHAR(512),
  id_card_back VARCHAR(512),
  real_name VARCHAR(32),
  id_card_number VARCHAR(32),
  status INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  UNIQUE KEY uk_open_id (open_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 房源表
CREATE TABLE IF NOT EXISTS houses (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(60) NOT NULL,
  type VARCHAR(20),
  area DECIMAL(10,2),
  price DECIMAL(10,2) NOT NULL,
  deposit VARCHAR(32),
  start_date DATE,
  end_date DATE,
  available_date DATE,
  description VARCHAR(500),
  images VARCHAR(2000),
  loc_province VARCHAR(64),
  loc_city VARCHAR(64),
  loc_district VARCHAR(64),
  loc_address VARCHAR(256),
  loc_latitude DOUBLE,
  loc_longitude DOUBLE,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  reject_reason VARCHAR(256),
  view_count INT DEFAULT 0,
  favorite_count INT DEFAULT 0,
  create_time DATETIME,
  update_time DATETIME,
  KEY idx_user_id (user_id),
  KEY idx_status (status),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 收藏表
CREATE TABLE IF NOT EXISTS favorites (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  house_id BIGINT NOT NULL,
  create_time DATETIME,
  UNIQUE KEY uk_user_house (user_id, house_id),
  KEY idx_user_id (user_id),
  KEY idx_house_id (house_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 消息表
CREATE TABLE IF NOT EXISTS messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  from_user_id BIGINT NOT NULL,
  to_user_id BIGINT NOT NULL,
  content VARCHAR(1000),
  msg_type VARCHAR(20) DEFAULT 'text',
  house_id BIGINT,
  is_read TINYINT(1) DEFAULT 0,
  create_time DATETIME,
  KEY idx_from_to (from_user_id, to_user_id),
  KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 举报表
CREATE TABLE IF NOT EXISTS complaints (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  complainant_id BIGINT NOT NULL,
  target_type VARCHAR(20) NOT NULL,
  target_id BIGINT NOT NULL,
  reason VARCHAR(64),
  description VARCHAR(500),
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  result VARCHAR(256),
  create_time DATETIME,
  update_time DATETIME,
  KEY idx_complainant (complainant_id),
  KEY idx_target (target_type, target_id),
  KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审核记录表
CREATE TABLE IF NOT EXISTS audits (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  target_type VARCHAR(32) NOT NULL,
  target_id BIGINT NOT NULL,
  auditor_id BIGINT,
  result VARCHAR(20) NOT NULL,
  reason VARCHAR(256),
  create_time DATETIME,
  KEY idx_target (target_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 管理员表
CREATE TABLE IF NOT EXISTS admin_users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL,
  password VARCHAR(128) NOT NULL,
  name VARCHAR(32),
  create_time DATETIME,
  update_time DATETIME,
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
