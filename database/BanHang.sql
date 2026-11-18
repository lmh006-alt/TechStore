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
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `categoryId` int NOT NULL AUTO_INCREMENT,
  `categoryname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `images` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Iphone','iphone.jpg',1);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `cate_id` int NOT NULL AUTO_INCREMENT,
  `cate_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `icons` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`cate_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (2,'Điện thoại','phone.png'),(3,'Laptop','laptop.png'),(4,'Máy tính bảng','tablet.png'),(5,'Tai nghe','headphone.png'),(6,'Bàn phím','keyboard.png'),(7,'Chuột máy tính','mouse.png'),(8,'Màn hình','monitor.png'),(9,'Loa Bluetooth','speaker.png'),(10,'Đồng hồ thông minh','watch.png'),(11,'Phụ kiện','accessories.png');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fullname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `roleid` int DEFAULT '2',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) NOT NULL DEFAULT '0',
  `otp_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `otp_expiry` datetime DEFAULT NULL,
  `reset_otp_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reset_otp_expiry` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'hoang0347373783@gmail.com','hoang','lmh','u5qhkT2ucH34oMyd6AYndw==:tgcWP/9nMT4XDueM+olyuSwGehKJHYKaZV+tmNQ0ZRw=',NULL,2,'0349461264','2026-11-10 14:16:10',0,NULL,NULL,NULL,NULL),(2,'lhdeptrai.16082006@gmail.com','lmh','lmh','qnIA/siAbMHzyHSrhaic2Q==:yobYsYse3CS2sminwILOgRAdSNn5mHI09LhRvqArw1o=',NULL,2,'0349461264','2026-11-16 14:30:15',1,NULL,NULL,NULL,NULL),(3,'leminhhoang.16082006@gmail.com','test','lmh','KyjbpLMPb8PSagaaRHDSag==:Rmnb74/QsLFfOMAd9OKFAi9Gqc5DwSfME4mBhaVHpNQ=',NULL,2,'0349461264','2026-11-16 15:03:39',1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videos`
--

DROP TABLE IF EXISTS `videos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `file_path` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `videos_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videos`
--

LOCK TABLES `videos` WRITE;
/*!40000 ALTER TABLE `videos` DISABLE KEYS */;
INSERT INTO `videos` VALUES (9,'Khám phá thiết kế laptop hiện đại','Cận cảnh thiết kế bàn phím và ngoại hình của laptop hiện đại.','8aed4e06-6720-4876-a0c3-cb8d8c4109a4.mp4','2026-11-18 18:00:55',2),(10,'Trải nghiệm bàn phím cơ gaming','Khám phá trải nghiệm gõ phím trên bàn phím cơ dành cho game thủ','6c50e4bd-09f6-47e7-b090-16ffcb6eaffe.mp4','2026-11-18 18:01:27',2),(11,'Bàn phím cơ LED RGB','Khám phá hiệu ứng ánh sáng RGB đầy màu sắc trên bàn phím cơ.','aecec13d-cdf1-458c-b16d-8ccccd9e3a63.mp4','2026-11-18 18:06:07',2),(12,'Bộ đôi chuột và bàn phím gaming','Khám phá bộ phụ kiện gaming với hệ thống đèn LED nổi bật.','b427fc7b-a768-4505-b68e-7ce527eb04df.mp4','2026-11-18 18:06:38',2),(13,'Laptop dành cho dân văn phòng','Trải nghiệm sử dụng laptop trong công việc hằng ngày.','1250e0dd-e525-47db-b0cf-af6eef6126b5.mp4','2026-11-18 18:07:18',2),(14,'Laptop và tai nghe','Bộ thiết bị công nghệ dành cho không gian làm việc tối giản.','2745718d-8fef-49ce-afd5-06ea05587eee.mp4','2026-11-18 18:07:48',2),(15,'Bàn phím dành cho lập trình viên','Trải nghiệm viết mã Python với bàn phím cơ.','71868ede-81d5-4264-9b1e-240c70394e01.mp4','2026-11-18 18:08:17',2),(16,'Góc gaming đầy màu sắc','Cận cảnh bàn phím và chuột gaming với hiệu ứng ánh sáng RGB.','ba536c47-db88-4382-aa76-d1440bc1075b.mp4','2026-11-18 18:08:47',2),(17,'Khám phá bàn phím laptop','Trải nghiệm thao tác trên bàn phím laptop có đèn nền.','ceaad2a7-ac30-4f22-8dcd-8799226071ac.mp4','2026-11-18 18:09:24',2),(18,'Laptop trong cuộc sống hằng ngày','Khám phá trải nghiệm sử dụng laptop phục vụ học tập và công việc.','77f22da1-73f1-4ba1-867b-c8c311f53902.mp4','2026-11-18 18:09:55',2);
/*!40000 ALTER TABLE `videos` ENABLE KEYS */;
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

-- Dump completed on 2026-11-18 18:39:39
