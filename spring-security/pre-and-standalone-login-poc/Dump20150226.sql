CREATE DATABASE  IF NOT EXISTS `taskmanager` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `taskmanager`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: taskmanager
-- ------------------------------------------------------
-- Server version	5.6.22-log

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
-- Table structure for table `line_item`
--

DROP TABLE IF EXISTS `line_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `line_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task` bigint(20) NOT NULL,
  `task_owner` bigint(20) NOT NULL,
  `line_item_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_line_item_1_idx` (`task`),
  KEY `fk_line_item_2_idx` (`task_owner`),
  CONSTRAINT `fk_line_item_1` FOREIGN KEY (`task`) REFERENCES `task` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_line_item_2` FOREIGN KEY (`task_owner`) REFERENCES `task_owner` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `line_item`
--

LOCK TABLES `line_item` WRITE;
/*!40000 ALTER TABLE `line_item` DISABLE KEYS */;
INSERT INTO `line_item` VALUES (1,1,1,'requirement_item1'),(2,1,2,'requirement_item2'),(3,2,1,'backlog_item1'),(4,2,2,'backlog_item2');
/*!40000 ALTER TABLE `line_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_master`
--

DROP TABLE IF EXISTS `role_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role` varchar(255) DEFAULT NULL,
  `role_desc` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_master`
--

LOCK TABLES `role_master` WRITE;
/*!40000 ALTER TABLE `role_master` DISABLE KEYS */;
INSERT INTO `role_master` VALUES (1,'ADMIN','Admin role','Admin'),(2,'USER','User role','User'),(3, 'AUTHOR', 'Author Role', 'Author');
/*!40000 ALTER TABLE `role_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_archived` int(11) DEFAULT NULL,
  `task_description` varchar(255) DEFAULT NULL,
  `task_name` varchar(255) DEFAULT NULL,
  `task_priority` varchar(255) DEFAULT NULL,
  `task_status` varchar(255) DEFAULT NULL,
  `task_expense` float DEFAULT NULL,
  `task_start_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,0,'Gathering Requirement','Requirement Gathering','MEDIUM','ACTIVE'),(2,0,'Gathering Backlog','Backlog Gathering','HIGH','COMPLETED'),(3,0,'Future scope','Future scope','HIGH','COMPLETED'),(4,0,'Defect tracking','Defect tracking','HIGH','COMPLETED');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_owner`
--

DROP TABLE IF EXISTS `task_owner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_owner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_owner`
--

LOCK TABLES `task_owner` WRITE;
/*!40000 ALTER TABLE `task_owner` DISABLE KEYS */;
INSERT INTO `task_owner` VALUES (1,'PrashatP'),(2,'ShravanT');
/*!40000 ALTER TABLE `task_owner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `urlconfig`
--

DROP TABLE IF EXISTS `urlconfig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `urlconfig` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `urlconfig`
--

LOCK TABLES `urlconfig` WRITE;
/*!40000 ALTER TABLE `urlconfig` DISABLE KEYS */;
INSERT INTO `urlconfig` VALUES (1,'Access Denied','PERMIT_ALL','/'),(2,'Logout','PERMIT_ALL','/logout'),(3,'Favicon','PERMIT_ALL','/favicon.ico'),(4,'All','AUTHENTICATED','/**'),(5,'RESOURCES','WEB_RESOURCES','/libs/**'),(6,'Static','WEB_RESOURCES','/static/**'),(7,'Js','WEB_RESOURCES','/js/**'),(8,'CSS','WEB_RESOURCES','/css/**'),(9,'WEB_RESOURCES','WEB_RESOURCES','/images/**'),(10,'Entry Point','ENTRY_POINT_URL','/'),(11,'ng-resouces','WEB_RESOURCES','/app/**'),(12,'fonts','WEB_RESOURCES','/fonts/**'),(13,'templates','WEB_RESOURCES','/templates/**'),(14,'Logout','LOGOUT_URL','/logout'),(15,'Logout','LOGOUT_SUCCESS_URL','/');
/*!40000 ALTER TABLE `urlconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'','Amita','Vadhavkar','b1f4d366469b521aa8bb61f17e450e38dd805127ccfd0b0ab46f8c5225ff9aaa','b7811e52b67ec79374ac54c21e4efdec727c01766ba27865','amita.vadhavkar@bioanalytic.net'),(2,'','Kuldeep','Bhutange','316f016d38c35cdd20ee550249a6937cd5be41b1c2d12449a91dbceac3667942','63639cb120be38b4eb64abb7f982f08bfc6babdbc767de3b','kbhutange@bioanalytic.net');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_it77eq964jhfqtu54081ebtio` (`role_id`),
  CONSTRAINT `FK_apcc8lxk2xnug8377fatvbn04` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_it77eq964jhfqtu54081ebtio` FOREIGN KEY (`role_id`) REFERENCES `role_master` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,1),(2,2),(2,3);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `taskmanager`.`taskdetail` AS 
select `t`.`id` AS `task_id`,`t`.`task_name` AS `task_name`,`l`.`id` AS `line_item_id`,`l`.`line_item_name` AS `line_item_name`
from (`taskmanager`.`task` `t` join `taskmanager`.`line_item` `l`) where (`t`.`id` = `l`.`task`);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-02-26 11:32:52
