DROP DATABASE IF EXISTS `store_db`;
CREATE DATABASE IF NOT EXISTS `store_db`;

CREATE TABLE users_tb(
	`id` BIGINT AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` CHAR(60) NOT NULL,
    `role` ENUM ("cliente", "vendedor") NOT NULL,
    `createdAt` DATE DEFAULT (CURRENT_DATE),
    
    PRIMARY KEY(`id`)
);

CREATE TABLE stores_tb(
	`id` BIGINT AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `desc` TEXT NOT NULL,
    `ownerId` BIGINT NOT NULL UNIQUE,
    `createdAt` DATE DEFAULT (CURRENT_DATE),
    
    PRIMARY KEY(`id`),
    FOREIGN KEY (`owner_id`) REFERENCES users_tb.id
);

CREATE TABLE products_tb(
	`id` BIGINT AUTO_INCREMENT,
	`name` VARCHAR(100) NOT NULL UNIQUE,
	`desc` TEXT NOT NULL,
	`price` DECIMAL(8,2) NOT NULL,
	`qtd` INT NOT NULL,
	`imgUrl` VARCHAR(255),
	`createdAt` DATE DEFAULT (CURRENT_DATE),
	PRIMARY KEY(`id`),
	FOREIGN KEY (`store_id`) REFERENCES stores_tb.id
);
