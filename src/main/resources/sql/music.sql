/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : douban

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-08-31 20:24:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for music
-- ----------------------------
DROP TABLE IF EXISTS `music`;
CREATE TABLE `music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `url` varchar(1000) NOT NULL,
  `url_md5` varchar(70) NOT NULL,
  `singer` varchar(30) DEFAULT NULL,
  `comment` int(11) DEFAULT NULL,
  `score` float DEFAULT NULL,
  `publish_time` varchar(100) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `bk_field1` varchar(255) DEFAULT NULL,
  `bk_field2` varchar(255) DEFAULT NULL,
  `bk_field3` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_url_md5` (`url_md5`),
  KEY `index_category` (`category`),
  KEY `index_tag` (`tag`),
  KEY `index_score` (`score`),
  KEY `index_comment` (`comment`)
) ENGINE=InnoDB AUTO_INCREMENT=2873 DEFAULT CHARSET=utf8;
