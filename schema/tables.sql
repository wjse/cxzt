/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : cxzt

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 04/12/2019 23:05:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_invoice
-- ----------------------------
DROP TABLE IF EXISTS `t_invoice`;
CREATE TABLE `t_invoice` (
  `id` bigint(20) NOT NULL,
  `number` char(15) NOT NULL COMMENT '发票号码',
  `code` char(15) NOT NULL COMMENT '发票代码',
  `date` datetime NOT NULL COMMENT '开票日期',
  `type` int(4) NOT NULL COMMENT '发票类型:1-专票，2-普票',
  `invoice_type` int(4) NOT NULL COMMENT '票据类型:1-进项票，2-成本票',
  `buyer_name` varchar(100) NOT NULL COMMENT '购买方名称',
  `buyer_tax_no` varchar(50) NOT NULL COMMENT '购买方纳税人识别号',
  `buyer_addr_tel` varchar(255) NOT NULL COMMENT '购买方地址电话',
  `buyer_bank` varchar(255) NOT NULL COMMENT '购买方银行及账号',
  `seller_name` varchar(100) NOT NULL COMMENT '销售方名称',
  `seller_tax_no` varchar(50) NOT NULL COMMENT '销售方纳税人识别号',
  `seller_addr_tel` varchar(255) NOT NULL COMMENT '销售方地址电话',
  `seller_bank` varchar(255) NOT NULL COMMENT '销售方银行及账号',
  `amount_tax` double DEFAULT NULL COMMENT '价税合计',
  `amount` double DEFAULT NULL COMMENT '金额合计',
  `tax_amount` double DEFAULT NULL COMMENT '税额合计',
  `package_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_invoice_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_invoice_detail`;
CREATE TABLE `t_invoice_detail` (
  `id` bigint(20) NOT NULL,
  `remark` varchar(255) NOT NULL COMMENT '摘要',
  `specs` varchar(50) DEFAULT NULL COMMENT '规格型号',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `count` double DEFAULT NULL COMMENT '数量',
  `price` double DEFAULT NULL COMMENT '单价',
  `amount` double DEFAULT NULL COMMENT '金额',
  `tax_rate` double DEFAULT NULL COMMENT '税率',
  `tax_amount` double DEFAULT NULL COMMENT '税额',
  `invoice_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_invoice_package
-- ----------------------------
DROP TABLE IF EXISTS `t_invoice_package`;
CREATE TABLE `t_invoice_package` (
  `id` bigint(20) NOT NULL,
  `date` datetime NOT NULL COMMENT '提交日期',
  `user_id` int(11) NOT NULL COMMENT '提交人',
  `count` int(11) DEFAULT NULL COMMENT '发票张数'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `mobile` char(15) NOT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `type` enum('ADMIN','NORMAL') DEFAULT NULL COMMENT '用户类型',
  `open_id` varchar(100) DEFAULT NULL COMMENT '微信id',
  `region` varchar(100) DEFAULT NULL COMMENT '所属区域',
  `company` varchar(100) DEFAULT NULL COMMENT '公司',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
