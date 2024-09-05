/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.123.34_3306
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 192.168.123.34:3306
 Source Schema         : db_read_share_and_buy

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 05/09/2024 22:26:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_admin
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin`;
CREATE TABLE `tb_admin`  (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `admin_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10001 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_admin
-- ----------------------------
INSERT INTO `tb_admin` VALUES (10000, 'testName', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095');

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `article_id` bigint NOT NULL,
  `article_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `article_context_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `article_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `article_state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES (1831695980921217024, '罗技g703评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1831695980946382848.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/SD005D.png', '未审核', 10000, '2024-09-05 22:08:48');
INSERT INTO `tb_article` VALUES (1831696889055133696, '罗技g102评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1831696889084493824.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/SD005D.png', '未审核', 10000, '2024-09-05 22:12:25');
INSERT INTO `tb_article` VALUES (1831697207713083392, '罗技g502评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1831697207742443520.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/SD005D.png', '未审核', 10000, '2024-09-05 22:13:40');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `comment_id` bigint NOT NULL,
  `target_id` int NULL DEFAULT NULL,
  `comment_context` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `good_count` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------

-- ----------------------------
-- Table structure for tb_goods
-- ----------------------------
DROP TABLE IF EXISTS `tb_goods`;
CREATE TABLE `tb_goods`  (
  `goods_id` bigint NOT NULL COMMENT 'id',
  `goods_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '物品名',
  `goods_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型 鼠标/键盘等',
  `goods_count` int NULL DEFAULT NULL COMMENT '商品数量',
  `goods_price` double NULL DEFAULT NULL COMMENT '商品价格',
  `goods_connect_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '连接类型 有线/无线',
  `goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `goods_introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品描述',
  `goods_brand` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品品牌',
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_goods
-- ----------------------------
INSERT INTO `tb_goods` VALUES (1829129094807257088, '罗技G304', '鼠标', 5, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1829129094840811520.html', '罗技');
INSERT INTO `tb_goods` VALUES (1829129110573645824, '罗技G102', '鼠标', 6, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1829129110582034432.html', '罗技');
INSERT INTO `tb_goods` VALUES (1829129125190795264, '罗技G502', '鼠标', 5, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1829129125199183872.html', '罗技');
INSERT INTO `tb_goods` VALUES (1829129140483227648, '罗技G703', '鼠标', 0, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1829129140495810560.html', '罗技');
INSERT INTO `tb_goods` VALUES (1831689847858892800, '罗技G503', '鼠标', 10, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1831689847867281408.html', '罗技');

-- ----------------------------
-- Table structure for tb_order
-- ----------------------------
DROP TABLE IF EXISTS `tb_order`;
CREATE TABLE `tb_order`  (
  `out_trade_no` bigint NOT NULL,
  `user_id` int NULL DEFAULT NULL,
  `goods_id` bigint NULL DEFAULT NULL,
  `price` double(10, 2) NULL DEFAULT NULL,
  `goods_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `state` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `recipient` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `goods_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `goods_count` int NULL DEFAULT NULL,
  PRIMARY KEY (`out_trade_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (1830125634031976448, 10000, 1829129110573645824, 100.00, '罗技G102', '已完成', '2024-09-01 14:08:48', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1);
INSERT INTO `tb_order` VALUES (1830130397461135360, 10000, 1829129110573645824, 100.00, '罗技G103', '已支付', '2024-09-01 14:27:44', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1);
INSERT INTO `tb_order` VALUES (1830137924559134720, 10000, 1829129110573645824, 100.00, '罗技G104', '已支付', '2024-09-01 14:57:38', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1);
INSERT INTO `tb_order` VALUES (1830163936546140160, 10000, 1829129110573645824, 100.00, '罗技G102', '已关闭', '2024-09-01 16:41:00', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `recipient` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sign` varchar(144) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (10000, 'mata', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095', '2393821643@qq.com', 'addressT', 'test', 'w', '男', '18052223421', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/headImg/邮电部诗人.png');
INSERT INTO `tb_user` VALUES (10001, '用户124124', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095', '', 'test', NULL, NULL, '男', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
