-- MySQL dump 10.13  Distrib 5.6.35, for macos10.12 (x86_64)
--
-- Host: localhost    Database: gmh
-- ------------------------------------------------------
-- Server version	5.6.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `staff`
--

DROP TABLE IF EXISTS `staff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL COMMENT '邮箱',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(255) NOT NULL COMMENT '盐值',
  `store_id` bigint(20) NOT NULL COMMENT '店铺id',
  `principal_name` varchar(255) NOT NULL COMMENT '负责人姓名',
  `principal_mobile` varchar(255) NOT NULL COMMENT '负责人电话',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff`
--

/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;

--
-- Table structure for table `staff_token`
--

DROP TABLE IF EXISTS `staff_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `staff_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `staff_id` bigint(20) NOT NULL COMMENT '人员id',
  `token` varchar(255) NOT NULL COMMENT 'token',
  `login_time` datetime NOT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_UNIQUE` (`token`) USING BTREE,
  UNIQUE KEY `staff_id_UNIQUE` (`staff_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_token`
--

/*!40000 ALTER TABLE `staff_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff_token` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-28 22:21:36

DROP TABLE IF EXISTS `project_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `top_type` int(11) DEFAULT NULL COMMENT '顶层分类：\n1：美甲\n2：美睫\n3：美容\n4：产品',
  `type_name` varchar(45) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER TABLE `project_type` 
ADD COLUMN `store_id` BIGINT(20) NULL AFTER `id`;

CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) DEFAULT NULL,
  `project_name` varchar(45) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)

CREATE TABLE `stock_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `store_id` int(11) NOT NULL,
  `type_name` varchar(45) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)

CREATE TABLE `stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) DEFAULT NULL,
  `stock_name` varchar(45) DEFAULT NULL,
  `unit` varchar(45),
  `amount` int(11),
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)

CREATE TABLE `member_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `store_id` int(11) DEFAULT NULL,
  `level_name` varchar(45) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)

CREATE TABLE `member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `store_id` int(11) DEFAULT NULL,
  `level_id` int(11) DEFAULT NULL,
  `phone` VARCHAR(45) DEFAULT NULL,
  `member_name` varchar(45) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL COMMENT '0:女\n1：男',
  `birthday` date DEFAULT NULL,
  `join_date` date DEFAULT NULL,
  `valid_date` date DEFAULT NULL,
  `nail_money` float DEFAULT NULL COMMENT '美甲美睫储值',
  `beauty_money` float DEFAULT NULL COMMENT '美容、产品储值',
  `remark` varchar(200) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)

create view member_vo as
select * from member 
left join(
select id as pid,level_name from member_level) as t
on
member.level_id=t.pid

CREATE TABLE `appointment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `store_id` int(11) DEFAULT NULL,
  `member_id` int(11) DEFAULT NULL,
  `member_id` VARCHAR(25) DEFAULT NULL,
  `project_id` int(11) DEFAULT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `begin_time` timestamp(6) NULL DEFAULT NULL,
  `end_time` timestamp(6) NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '状态\n1：待完成\n2：已完成\n3：已取消',
  `is_line` int(11) DEFAULT NULL COMMENT '是否点排\n1：是\n0：否',
  `remark` VARCHAR(200) DEFAULT NULL,
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
)