CREATE DATABASE `masterdb` /*!40100 COLLATE 'utf8_unicode_ci' */

CREATE TABLE `masterdb.sample` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL COLLATE 'utf8_unicode_ci',
    PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;

CREATE DATABASE `slavedb` /*!40100 COLLATE 'utf8_unicode_ci' */

CREATE TABLE `slavedb.sample` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL COLLATE 'utf8_unicode_ci',
    PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8_unicode_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1
;