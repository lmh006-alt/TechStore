CREATE DATABASE  IF NOT EXISTS `banhang` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `banhang`;
-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: banhang
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Iphone','iphone.jpg',1);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (2,'Điện thoại','phone.png'),(3,'Laptop','laptop.png'),(4,'Máy tính bảng','tablet.png'),(5,'Tai nghe','headphone.png'),(6,'Bàn phím','keyboard.png'),(7,'Chuột máy tính','mouse.png'),(8,'Màn hình','monitor.png'),(9,'Loa Bluetooth','speaker.png'),(10,'Đồng hồ thông minh','watch.png'),(11,'Phụ kiện','accessories.png');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'hoang0347373783@gmail.com','hoang','lmh','u5qhkT2ucH34oMyd6AYndw==:tgcWP/9nMT4XDueM+olyuSwGehKJHYKaZV+tmNQ0ZRw=',NULL,2,'0349461264','2026-11-10 14:16:10',0,NULL,NULL,NULL,NULL),(2,'lhdeptrai.16082006@gmail.com','lmh','lmh','qnIA/siAbMHzyHSrhaic2Q==:yobYsYse3CS2sminwILOgRAdSNn5mHI09LhRvqArw1o=',NULL,2,'0349461264','2026-11-16 14:30:15',1,NULL,NULL,NULL,NULL),(3,'leminhhoang.16082006@gmail.com','test','lmh','KyjbpLMPb8PSagaaRHDSag==:Rmnb74/QsLFfOMAd9OKFAi9Gqc5DwSfME4mBhaVHpNQ=',NULL,2,'0349461264','2026-11-16 15:03:39',1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'banhang'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-16 15:09:41
