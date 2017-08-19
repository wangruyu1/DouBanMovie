/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : douban

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-08-19 11:44:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for movie
-- ----------------------------
DROP TABLE IF EXISTS `movie`;
CREATE TABLE `movie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `score` float DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  `url_md5` varchar(100) NOT NULL,
  `pic` varchar(1000) DEFAULT NULL,
  `bk_field1` varchar(255) DEFAULT NULL COMMENT '类型:tv,movie',
  `bk_field2` varchar(255) DEFAULT NULL COMMENT '分类:恐怖,悬疑...',
  `bk_field3` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_url_md5` (`url_md5`) USING BTREE,
  KEY `index_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13339 DEFAULT CHARSET=utf8;
