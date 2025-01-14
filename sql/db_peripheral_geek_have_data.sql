/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.50.226
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : 192.168.50.226:3306
 Source Schema         : db_read_share_and_buy

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 14/01/2025 14:00:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  `brief_introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES (1868581167642546176, '罗技aaa评测123', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868581167646740480.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/1861951-aa970c742e-00000026.jpg', '已审核', 10000, 'g102testtestadasd1424', '2024-12-16 16:57:22');
INSERT INTO `tb_article` VALUES (1868582144282038272, '罗技102评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868582144282038273.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/1861951-aa970c742e-00000026.jpg', '已审核', 10000, 'g102testadasd1424', '2024-12-16 17:01:15');
INSERT INTO `tb_article` VALUES (1868582200632512512, '罗技304评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868582200632512513.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/dc4_ev_asu04c.png', '已审核', 10000, 'g304testadasd1424', '2024-12-16 17:01:28');
INSERT INTO `tb_article` VALUES (1868582350008455168, '罗技g502评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868582350012649472.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/EV210A.png', '已审核', 10000, 'g502testadasd1424', '2024-12-16 17:02:04');
INSERT INTO `tb_article` VALUES (1868595512770400256, '罗技g903评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868595512774594560.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, 'g903testadasd1424', '2024-12-16 17:54:22');
INSERT INTO `tb_article` VALUES (1868826896806027264, '罗技g903评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868826896810221568.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 09:13:48');
INSERT INTO `tb_article` VALUES (1868826917047738368, '罗技g903 1评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868826917047738369.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 09:13:53');
INSERT INTO `tb_article` VALUES (1868826927491555328, '罗技g903 2评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868826927491555329.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 09:13:55');
INSERT INTO `tb_article` VALUES (1868826935540424704, '罗技g903 3评测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868826935540424705.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 09:13:57');
INSERT INTO `tb_article` VALUES (1868889964265205760, '10字测试测试测试测', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868889964273594368.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 13:24:25');
INSERT INTO `tb_article` VALUES (1868889994476777472, '20字测试测试测试测测试测试测试测试测试', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868889994476777473.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 13:24:32');
INSERT INTO `tb_article` VALUES (1868890024843538432, '30字测试测试测试测测试测试测试测试测试测试测试测试测试测试', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868890024843538433.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 13:24:39');
INSERT INTO `tb_article` VALUES (1868890069823254528, '50字测试测试测试测测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868890069823254529.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 13:24:50');
INSERT INTO `tb_article` VALUES (1868912911486885888, 'test123test', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868912911491080192.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:36');
INSERT INTO `tb_article` VALUES (1868912919275708416, 'test123test2', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868912919284097024.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:37');
INSERT INTO `tb_article` VALUES (1868912937122471936, 'test123test4', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868912937122471937.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:42');
INSERT INTO `tb_article` VALUES (1868912944445726720, 'test123tes5', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868912944445726721.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:43');
INSERT INTO `tb_article` VALUES (1868912951722844160, 'test123tes6', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868912951722844161.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:45');
INSERT INTO `tb_article` VALUES (1868912994265669632, 'test123tes21', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868912994265669633.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:55');
INSERT INTO `tb_article` VALUES (1868913001932857344, 'test123tes2156', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868913001932857345.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:57');
INSERT INTO `tb_article` VALUES (1868913008740212736, 'test123tes21562', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868913008740212737.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 14:55:59');
INSERT INTO `tb_article` VALUES (1868918506466123776, 'test123tes12', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868918506466123777.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 15:17:50');
INSERT INTO `tb_article` VALUES (1868918521641115648, 'test123tes14241', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868918521641115649.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 15:17:53');
INSERT INTO `tb_article` VALUES (1868918529186668544, 'test123t124', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868918529186668545.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 15:17:55');
INSERT INTO `tb_article` VALUES (1868918536119853056, 'test123t1241', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868918536119853057.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 15:17:57');
INSERT INTO `tb_article` VALUES (1868918545603174400, 'cesaafaw', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868918545603174401.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/20241105_141005.jpg', '审核未通过', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 15:17:59');
INSERT INTO `tb_article` VALUES (1868918560631365632, 'adgasdggd', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1868918560631365633.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/1861951-aa970c742e-00000026-gigapixel-cgi-2x.jpg', '已审核', 10000, '大家好，我是薄红。罗技的G900系列一直是玩家们所喜爱的一款鼠标，不过大家还是馋它的外观，但罗技靠着', '2024-12-17 15:18:02');
INSERT INTO `tb_article` VALUES (1871758039033421824, '测试测试测试测试123', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1872190512506671104.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/IMG_20241223_195942.png', '已审核', 10000, '12312312312312', '2024-12-25 11:21:09');
INSERT INTO `tb_article` VALUES (1872512067493253120, 'testTime', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1872512067497447424.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/dc4_ev_asa04a.png', '已审核', 10000, 'arawraw', '2024-12-27 13:17:22');
INSERT INTO `tb_article` VALUES (1872517930895380480, 'taserawawr', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1872579788687884288.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/dc4_ev_asa05h.png', '已审核', 10000, 'waradsad', '2024-12-27 13:40:39');
INSERT INTO `tb_article` VALUES (1876172518508736512, 'testOther', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/ArticleHtmlImg/1876172518508736513.html', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/articleImg/FYQel-LakAAMay0.jpg', '已审核', 10001, 'tseatastas', '2025-01-06 15:42:41');

-- ----------------------------
-- Table structure for tb_audit
-- ----------------------------
DROP TABLE IF EXISTS `tb_audit`;
CREATE TABLE `tb_audit`  (
  `article_id` bigint NOT NULL,
  `non_pass_cause` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_audit
-- ----------------------------
INSERT INTO `tb_audit` VALUES (1868918545603174400, 'test');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `comment_id` bigint NOT NULL,
  `target_id` bigint NULL DEFAULT NULL,
  `comment_context` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `good_count` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES (1872545787952504832, 1872517930895380480, '测试评论1', 10000, 1, '2024-12-27 15:31:21');
INSERT INTO `tb_comment` VALUES (1872545815001571328, 1872517930895380480, '测试评论2', 10000, 1, '2024-12-27 15:31:27');
INSERT INTO `tb_comment` VALUES (1872545824174514176, 1872517930895380480, '测试评论3', 10000, 1, '2024-12-27 15:31:29');
INSERT INTO `tb_comment` VALUES (1872563680807305216, 1872517930895380480, '测试评论4', 10000, 1, '2024-12-27 16:42:27');
INSERT INTO `tb_comment` VALUES (1872608756455522304, 1872517930895380480, '测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评论测试评', 10000, 1, '2024-12-27 19:41:34');
INSERT INTO `tb_comment` VALUES (1872609771414175744, 1872517930895380480, '测试评论5', 10000, 1, '2024-12-27 19:45:36');
INSERT INTO `tb_comment` VALUES (1872613208772132864, 1872545787952504832, '测试评论的评论1', 10000, 1, '2024-12-27 19:59:15');
INSERT INTO `tb_comment` VALUES (1872613219933175808, 1872545787952504832, '测试评论的评论2', 10000, 0, '2024-12-27 19:59:18');
INSERT INTO `tb_comment` VALUES (1872613228510527488, 1872545787952504832, '测试评论的评论3', 10000, 0, '2024-12-27 19:59:20');
INSERT INTO `tb_comment` VALUES (1872618032125263872, 1872563680807305216, 'test', 10000, 0, '2024-12-27 20:18:25');
INSERT INTO `tb_comment` VALUES (1872628315350712320, 1872545787952504832, 'test', 10000, 0, '2024-12-27 20:59:17');
INSERT INTO `tb_comment` VALUES (1872633820907388928, 1872545815001571328, 'test', 10000, 0, '2024-12-27 21:21:09');
INSERT INTO `tb_comment` VALUES (1872639499315527680, 1872512067493253120, 'test', 10000, 1, '2024-12-27 21:43:43');
INSERT INTO `tb_comment` VALUES (1872640320480559104, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:46:59');
INSERT INTO `tb_comment` VALUES (1872640347479293952, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:05');
INSERT INTO `tb_comment` VALUES (1872640350813765632, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:06');
INSERT INTO `tb_comment` VALUES (1872640353846247424, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:07');
INSERT INTO `tb_comment` VALUES (1872640355796598784, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:07');
INSERT INTO `tb_comment` VALUES (1872640495454339072, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:41');
INSERT INTO `tb_comment` VALUES (1872640501989064704, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:42');
INSERT INTO `tb_comment` VALUES (1872640507001257984, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:44');
INSERT INTO `tb_comment` VALUES (1872640511912787968, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:47:45');
INSERT INTO `tb_comment` VALUES (1872640666955235328, 1872517930895380480, 'test', 10000, 0, '2024-12-27 21:48:22');
INSERT INTO `tb_comment` VALUES (1872640675809411072, 1872517930895380480, '1872517930895380480', 10000, 0, '2024-12-27 21:48:24');
INSERT INTO `tb_comment` VALUES (1872640680486060032, 1872517930895380480, '1872517930895380480', 10000, 0, '2024-12-27 21:48:25');
INSERT INTO `tb_comment` VALUES (1872640684177047552, 1872517930895380480, '1872517930895380480', 10000, 0, '2024-12-27 21:48:26');
INSERT INTO `tb_comment` VALUES (1872640687863840768, 1872517930895380480, '1872517930895380480', 10000, 0, '2024-12-27 21:48:27');
INSERT INTO `tb_comment` VALUES (1872656501757276160, 1872517930895380480, 'test', 10000, 0, '2024-12-27 22:51:17');
INSERT INTO `tb_comment` VALUES (1873649473780981760, 1829129094807257088, 'test', 10000, 0, '2024-12-30 16:37:00');
INSERT INTO `tb_comment` VALUES (1877641071690792960, 1876653014808276992, 'test', 10000, 1, '2025-01-10 16:58:11');

-- ----------------------------
-- Table structure for tb_comment_good
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment_good`;
CREATE TABLE `tb_comment_good`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `comment_id` bigint NULL DEFAULT NULL,
  `user_id` bigint NULL DEFAULT NULL,
  `target_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment_good
-- ----------------------------
INSERT INTO `tb_comment_good` VALUES ('100001872545787952504832', 1872545787952504832, 10000, 1872517930895380480);
INSERT INTO `tb_comment_good` VALUES ('100001872545815001571328', 1872545815001571328, 10000, 1872517930895380480);
INSERT INTO `tb_comment_good` VALUES ('100001872545824174514176', 1872545824174514176, 10000, 1872517930895380480);
INSERT INTO `tb_comment_good` VALUES ('100001872563680807305216', 1872563680807305216, 10000, 1872517930895380480);
INSERT INTO `tb_comment_good` VALUES ('100001872608756455522304', 1872608756455522304, 10000, 1872517930895380480);
INSERT INTO `tb_comment_good` VALUES ('100001872609771414175744', 1872609771414175744, 10000, 1872517930895380480);
INSERT INTO `tb_comment_good` VALUES ('100001872613208772132864', 1872613208772132864, 10000, 1872545787952504832);
INSERT INTO `tb_comment_good` VALUES ('100001872639499315527680', 1872639499315527680, 10000, 1872512067493253120);
INSERT INTO `tb_comment_good` VALUES ('100001877641071690792960', 1877641071690792960, 10000, 1876653014808276992);

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
INSERT INTO `tb_goods` VALUES (1829129094807257088, '罗技G304', '鼠标', 12, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa09b.png', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1829129094840811520.html', '罗技');
INSERT INTO `tb_goods` VALUES (1829129110573645824, 'g304', '鼠标', 11, 102.1, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/ev_4fd_asu02b-gigapixel-cgi-2x.png', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1876624476533174272.html', '罗技');
INSERT INTO `tb_goods` VALUES (1829129125190795264, '罗技G502', '鼠标', 7, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa05a.png', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1829129125199183872.html', '罗技');
INSERT INTO `tb_goods` VALUES (1831689847858892800, '罗技G503', '鼠标', 11, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1831689847867281408.html', '罗技');
INSERT INTO `tb_goods` VALUES (1873622486219833344, '罗技G703', '鼠标', 9992, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1873622486224027648.html', '罗技');
INSERT INTO `tb_goods` VALUES (1876653014808276992, '测试商品test', 'test类型', 10, 100, '有线', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa21i.png', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsHtmlImg/1876653014841831424.html', '测试商品');

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
  `courier_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`out_trade_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_order
-- ----------------------------
INSERT INTO `tb_order` VALUES (1830125634031976448, 10000, 1829129110573645824, 100.00, '罗技G102', '已完成', '2024-09-01 14:08:48', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, 'test12312');
INSERT INTO `tb_order` VALUES (1830130397461135360, 10000, 1829129110573645824, 100.00, '罗技G103', '待发货', '2024-09-01 14:27:44', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1830137924559134720, 10000, 1829129110573645824, 100.00, '罗技G104', '待发货', '2024-09-01 14:57:38', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1830163936546140160, 10000, 1829129110573645824, 100.00, '罗技G102', '已关闭', '2024-09-01 16:41:00', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1866048715090841600, 10000, 1829129110573645824, 100.00, '罗技G102', '未支付', '2024-12-09 17:14:18', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1866048824700588032, 10000, 1829129110573645824, 100.00, '罗技G102', '未支付', '2024-12-09 17:14:44', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1873301435283791872, 10000, 1829129094807257088, 100.00, '罗技G304', '未支付', '2024-12-29 17:34:01', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1873668504554586112, 10000, 1829129125190795264, 100.00, '罗技G502', '未支付', '2024-12-30 17:52:37', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1873668691331137536, 10000, 1829129125190795264, 100.00, '罗技G502', '未支付', '2024-12-30 17:53:22', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1873668722054414336, 10000, 1829129125190795264, 100.00, '罗技G502', '未支付', '2024-12-30 17:53:29', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1873668784801202176, 10000, 1829129125190795264, 100.00, '罗技G502', '未支付', '2024-12-30 17:53:44', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1873669224771108864, 10000, 1873622486219833344, 100.00, '罗技G703testtesttest', '未支付', '2024-12-30 17:55:29', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/alisu.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1873669235118456832, 10000, 1873622486219833344, 100.00, '罗技G703testtesttest', '已完成', '2024-12-30 17:55:31', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/alisu.png', 1, 'testcourierCode123');
INSERT INTO `tb_order` VALUES (1873669775030325248, 10000, 1873622486219833344, 100.00, '罗技G703testtesttest', '待发货', '2024-12-30 17:57:40', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/alisu.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1873669988658802688, 10000, 1873622486219833344, 100.00, '罗技G703testtesttest', '待发货', '2024-12-30 17:58:31', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/alisu.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1873670027590332416, 10000, 1873622486219833344, 100.00, '罗技G703testtesttest', '未支付', '2024-12-30 17:58:40', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/alisu.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1874292078743252992, 10000, 1829129110573645824, 100.00, '罗技G102', '已完成', '2025-01-01 11:10:29', 'testaddress', 'testR', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, 'test123');
INSERT INTO `tb_order` VALUES (1874292678042185728, 10000, 1829129110573645824, 100.00, '罗技G102', '已完成', '2025-01-01 11:12:52', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, 'test');
INSERT INTO `tb_order` VALUES (1874395569910276096, 10000, 1829129125190795264, 100.00, '罗技G502', '已完成', '2025-01-01 18:01:43', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, 'testcourierCode123');
INSERT INTO `tb_order` VALUES (1874395747446775808, 10000, 1829129110573645824, 100.00, '罗技G102', '已关闭', '2025-01-01 18:02:25', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1874783999097892864, 10000, 1831689847858892800, 100.00, '罗技G503', '未支付', '2025-01-02 19:45:12', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1874784361259266048, 10000, 1829129140483227648, 200.20, '罗技G703', '未支付', '2025-01-02 19:46:38', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1874784431098621952, 10000, 1873622486219833344, 100.00, '罗技G703testtesttest', '未支付', '2025-01-02 19:46:55', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/alisu.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876539805011132416, 10000, 1829129110573645824, 100.00, '罗技G102', '已关闭', '2025-01-07 16:02:09', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1876653790653853696, 10000, 1876653014808276992, 100.00, '测试商品test', '已关闭', '2025-01-07 23:35:05', 'test', 'test', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa21i.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876660787545673728, 10000, 1831689847858892800, 100.00, '罗技G503', '已关闭', '2025-01-08 00:02:53', '23423', '24124', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 1, NULL);
INSERT INTO `tb_order` VALUES (1876661705188061184, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:06:32', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876662391502024704, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:09:15', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876662922098245632, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:11:22', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876663978270130176, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:15:34', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876664315475394560, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:16:54', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876665631245348864, 10000, 1876653014808276992, 100.00, '测试商品test', '已关闭', '2025-01-08 00:22:08', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa21i.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876666889104535552, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:27:08', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876667356949774336, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:28:59', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876667742834241536, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:30:31', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876668129825783808, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 00:32:04', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876679141614518272, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 01:15:49', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876680419455283200, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 01:20:54', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876680791817220096, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 01:22:22', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1876681120747122688, 10000, 1873622486219833344, 100.00, '罗技G703', '已关闭', '2025-01-08 01:23:41', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa10c.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1877649941024829440, 10000, 1831689847858892800, 200.00, '罗技G503', '已关闭', '2025-01-10 17:33:26', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/fc0d789c683f9a8c.jpg', 2, NULL);
INSERT INTO `tb_order` VALUES (1877691366760251392, 10000, 1829129094807257088, 100.00, '罗技G304', '未支付', '2025-01-10 20:18:02', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa09b.png', 1, NULL);
INSERT INTO `tb_order` VALUES (1878674884873158656, 10000, 1829129094807257088, 100.00, '罗技G304', '未支付', '2025-01-13 13:26:11', 'testaddtest', 'mata', '18050173399', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/goodsImg/dc4_ev_asa09b.png', 1, NULL);

-- ----------------------------
-- Table structure for tb_receipt_information
-- ----------------------------
DROP TABLE IF EXISTS `tb_receipt_information`;
CREATE TABLE `tb_receipt_information`  (
  `user_id` int NOT NULL,
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `recipient` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_receipt_information
-- ----------------------------
INSERT INTO `tb_receipt_information` VALUES (10000, '18050173399', 'testaddtest', 'mata');

-- ----------------------------
-- Table structure for tb_recommend_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_recommend_article`;
CREATE TABLE `tb_recommend_article`  (
  `article_id` bigint NOT NULL,
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_recommend_article
-- ----------------------------
INSERT INTO `tb_recommend_article` VALUES (1868542685117239296);
INSERT INTO `tb_recommend_article` VALUES (1868542755703181312);
INSERT INTO `tb_recommend_article` VALUES (1868544508041449472);
INSERT INTO `tb_recommend_article` VALUES (1868545644043546624);
INSERT INTO `tb_recommend_article` VALUES (1868582144282038272);
INSERT INTO `tb_recommend_article` VALUES (1868582200632512512);
INSERT INTO `tb_recommend_article` VALUES (1868582350008455168);
INSERT INTO `tb_recommend_article` VALUES (1876172518508736512);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, 'user');
INSERT INTO `tb_role` VALUES (2, 'admin');
INSERT INTO `tb_role` VALUES (3, 'normal_admin');

-- ----------------------------
-- Table structure for tb_shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `tb_shopping_cart`;
CREATE TABLE `tb_shopping_cart`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `goods_id` bigint NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  `goods_will_buy_count` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10004 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_shopping_cart
-- ----------------------------
INSERT INTO `tb_shopping_cart` VALUES (1, 1829129094807257088, 10000, 1);
INSERT INTO `tb_shopping_cart` VALUES (10002, 1876653014808276992, 10000, 1);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sign` varchar(144) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `head_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10010 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (10000, 'mata', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095', '2393821643@qq.com', 'testtest测试测试123', '男', 'https://peripheral-geek-1314180676.cos.ap-guangzhou.myqcloud.com/headImg/dc4_ev_asa04a.png', 1);
INSERT INTO `tb_user` VALUES (10001, '用户124124', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095', '', NULL, '男', NULL, 1);
INSERT INTO `tb_user` VALUES (10002, 'admin', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095', NULL, NULL, NULL, NULL, 2);
INSERT INTO `tb_user` VALUES (10004, 'testadmin', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095', NULL, NULL, NULL, NULL, 3);
INSERT INTO `tb_user` VALUES (10006, 'main', '91a7adde5b0919d53ffb7dc7253f9f345c3c902a759fe5a2493c70abb7e25095', NULL, NULL, NULL, NULL, 3);
INSERT INTO `tb_user` VALUES (10007, 'mata2', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `tb_user` VALUES (10008, 'mata3', NULL, NULL, NULL, NULL, NULL, 1);
INSERT INTO `tb_user` VALUES (10009, 'mata4', NULL, NULL, NULL, NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
