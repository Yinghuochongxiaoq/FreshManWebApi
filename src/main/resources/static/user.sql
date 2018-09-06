/*
Navicat MySQL Data Transfer

Source Server         : MySqlRoot
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : costnote

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2018-09-06 10:51:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Sex` varchar(255) DEFAULT NULL,
  `Birthday` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'FreshMan', 'e10adc3949ba59abbe56e057f20f883e', '1', '1991-06-07 18:55:19');
