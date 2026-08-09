USE sekai_friend;

ALTER TABLE sekai_2048_user
    ADD COLUMN IF NOT EXISTS vip_level VARCHAR(32) NULL AFTER avatar,
    ADD COLUMN IF NOT EXISTS vip_expire_time DATETIME NULL AFTER vip_level;

UPDATE sekai_2048_user
SET vip_level = 'VIP',
    vip_expire_time = DATE_ADD(NOW(), INTERVAL 1 YEAR)
WHERE username = 'sekai';

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
