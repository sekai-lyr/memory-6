CREATE DATABASE IF NOT EXISTS sekai_friend DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sekai_friend;

CREATE TABLE IF NOT EXISTS sekai_2048_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(64) NOT NULL,
    avatar VARCHAR(500),
    vip_level VARCHAR(32),
    vip_expire_time DATETIME,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sekai_2048_game_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    nickname VARCHAR(64) NOT NULL,
    score INT NOT NULL DEFAULT 0,
    max_tile INT NOT NULL DEFAULT 2,
    max_character VARCHAR(120) NOT NULL,
    move_count INT NOT NULL DEFAULT 0,
    duration_seconds INT NOT NULL DEFAULT 0,
    board_state TEXT,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_2048_record_user_time (user_id, create_time),
    INDEX idx_2048_record_rank (score, max_tile),
    CONSTRAINT fk_2048_record_user FOREIGN KEY (user_id) REFERENCES sekai_2048_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sekai_2048_monetization_lead (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    nickname VARCHAR(64) NOT NULL,
    lead_type VARCHAR(32) NOT NULL,
    offer_code VARCHAR(64) NOT NULL,
    contact_name VARCHAR(64) NOT NULL,
    contact_info VARCHAR(120) NOT NULL,
    company_name VARCHAR(120),
    budget_cents INT NOT NULL DEFAULT 0,
    message VARCHAR(500),
    status VARCHAR(32) NOT NULL DEFAULT 'NEW',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_2048_lead_user_time (user_id, create_time),
    INDEX idx_2048_lead_status_time (status, create_time),
    INDEX idx_2048_lead_type_time (lead_type, create_time),
    CONSTRAINT fk_2048_lead_user FOREIGN KEY (user_id) REFERENCES sekai_2048_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sekai_2048_vip_daily_gift (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    reward_date DATE NOT NULL,
    score_bonus INT NOT NULL DEFAULT 0,
    boost_bonus INT NOT NULL DEFAULT 0,
    scan_bonus INT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_2048_vip_gift_user_date (user_id, reward_date),
    INDEX idx_2048_vip_gift_time (create_time),
    CONSTRAINT fk_2048_vip_gift_user FOREIGN KEY (user_id) REFERENCES sekai_2048_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sekai_2048_vip_cloud_save (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    slot_name VARCHAR(64) NOT NULL,
    run_data MEDIUMTEXT NOT NULL,
    score INT NOT NULL DEFAULT 0,
    max_tile INT NOT NULL DEFAULT 2,
    move_count INT NOT NULL DEFAULT 0,
    duration_seconds INT NOT NULL DEFAULT 0,
    mode VARCHAR(32) NOT NULL DEFAULT 'classic',
    board_size INT NOT NULL DEFAULT 4,
    target_tile INT NOT NULL DEFAULT 2048,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_2048_vip_cloud_user_slot (user_id, slot_name),
    INDEX idx_2048_vip_cloud_user_time (user_id, update_time),
    CONSTRAINT fk_2048_vip_cloud_user FOREIGN KEY (user_id) REFERENCES sekai_2048_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
