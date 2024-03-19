-- 테이블 생성 sql 관리

-- 유저 테이블
CREATE TABLE user (
    `created_at` datetime(6) DEFAULT NULL,
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `email` varchar(255) NOT NULL,
    `member_status` varchar(255) NOT NULL,
    `member_type` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `password` varchar(255) NOT NULL,
    `phone` varchar(255) NOT NULL,
    `refreshToken` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY  (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 토큰 테이블
CREATE TABLE token (
     `expiration` int(11) DEFAULT NULL,
     `id` bigint(20) NOT NULL,
     `refresh_token` varchar(255) DEFAULT NULL,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 공고 테이블
CREATE TABLE post (
    `created_at` timestamp NULL DEFAULT current_timestamp(),
    `post_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `shelter_id` bigint(20) DEFAULT NULL,
    `updated_at` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
    `image_name` varchar(255) DEFAULT NULL,
    `image_url` varchar(255) DEFAULT NULL,
    `status` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- bom info 테이블

CREATE TABLE bominfo (
    `created_at` timestamp NULL DEFAULT current_timestamp(),
    `info_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `post_id` bigint(20) NOT NULL,
    `updated_at` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
    `age` varchar(255) DEFAULT NULL,
    `breed` varchar(255) DEFAULT NULL,
    `extra` varchar(255) DEFAULT NULL,
    `gender` varchar(255) DEFAULT NULL,
    `name` varchar(255) DEFAULT NULL,
    `personality` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`info_id`),
    UNIQUE KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 좋아요 테이블
CREATE TABLE likes (
    `member_id` bigint(20) NOT NULL,
    `post_id` bigint(20) NOT NULL,
    `created_at` datetime(6) default null,
    `updated_at` datetime(6) default null,
    PRIMARY KEY (member_id, post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 채팅 테이블
CREATE TABLE chat (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `post_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    `input` varchar(255) DEFAULT NULL,
    `response` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;