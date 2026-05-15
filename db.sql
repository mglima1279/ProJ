DROP DATABASE IF EXISTS `store_db`;

CREATE DATABASE IF NOT EXISTS `store_db`;

USE `store_db`;

CREATE TABLE users_tb (
    `id` BIGINT AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` CHAR(60) NOT NULL,
    `role` ENUM ("CLIENT", "OWNER") NOT NULL,
    `created_at` TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (`id`)
);

CREATE TABLE stores_tb (
    `id` BIGINT AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `desc` TEXT NOT NULL,
    `owner_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`owner_id`) REFERENCES users_tb (id)
);

CREATE TABLE products_tb (
    `id` BIGINT AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `desc` TEXT NOT NULL,
    `price` DECIMAL(8, 2) NOT NULL,
    `qtd` INT NOT NULL,
    `img_url` VARCHAR(255),
    `store_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`store_id`) REFERENCES stores_tb (id)
);

CREATE TABLE orders_tb (
    `id` BIGINT AUTO_INCREMENT,
    `client_id` BIGINT NOT NULL,
    `store_id` BIGINT NOT NULL,
    `status` ENUM (
        "PENDING",
        "PROCESSING",
        "SHIPPED",
        "DELIVERED",
        "CANCELED"
    ) NOT NULL,
    `value` DECIMAL(8, 2) NOT NULL,
    `created_at` TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`client_id`) REFERENCES users_tb (id),
    FOREIGN KEY (`store_id`) REFERENCES stores_tb (id)
);

CREATE TABLE product_order_tb (
    `id` BIGINT AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `qtd` INT NOT NULL,
    `und_value` DECIMAL(8, 2) NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`order_id`) REFERENCES orders_tb (id),
    FOREIGN KEY (`product_id`) REFERENCES products_tb (id)
);

CREATE TABLE payments_tb (
    `id` BIGINT AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `method` ENUM ("DEBIT", "CREDIT", "BOLETO", "PIX") NOT NULL,
    `status` ENUM ("PENDING", "CONFIRMED", "FAILED") NOT NULL,
    `value` DECIMAL(8, 2) NOT NULL,
    `created_at` TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`order_id`) REFERENCES orders_tb (id)
);

CREATE TABLE ratings_tb (
    `id` BIGINT AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `rate` INT NOT NULL,
    `comment` TEXT,
    `created_at` TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users_tb (id),
    FOREIGN KEY (`product_id`) REFERENCES products_tb (id)
);