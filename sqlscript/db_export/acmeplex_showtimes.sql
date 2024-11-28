-- MySQL dump 10.13  Distrib 8.0.38, for macos14 (arm64)
--
-- Host: localhost    Database: acmeplex
-- ------------------------------------------------------
-- Server version	9.0.1

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
-- Table structure for table `showtimes`
--

DROP TABLE IF EXISTS `showtimes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `showtimes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `session` int NOT NULL,
  `movie_id` bigint NOT NULL,
  `theater_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeltpyuei1d5g3n6ikpsjwwil6` (`movie_id`),
  KEY `FKlbd5v4ium9mjbwh7l8nbjqjhw` (`theater_id`),
  CONSTRAINT `FKeltpyuei1d5g3n6ikpsjwwil6` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`),
  CONSTRAINT `FKlbd5v4ium9mjbwh7l8nbjqjhw` FOREIGN KEY (`theater_id`) REFERENCES `theaters` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `showtimes`
--

LOCK TABLES `showtimes` WRITE;
/*!40000 ALTER TABLE `showtimes` DISABLE KEYS */;
INSERT INTO `showtimes` VALUES (1,'2024-11-29',1,1,1),(2,'2024-11-29',2,1,1),(3,'2024-11-29',1,1,2),(4,'2024-11-29',2,1,2),(5,'2024-11-29',1,1,3),(6,'2024-11-29',2,1,3),(7,'2024-11-29',1,2,1),(8,'2024-11-29',2,2,1),(9,'2024-11-29',1,2,2),(10,'2024-11-29',2,2,2),(11,'2024-11-29',1,2,3),(12,'2024-11-29',2,2,3),(13,'2024-12-04',1,1,1),(14,'2024-12-04',2,1,1),(15,'2024-12-04',1,1,2),(16,'2024-12-04',2,1,2),(17,'2024-12-04',1,1,3),(18,'2024-12-04',2,1,3),(19,'2024-12-04',1,2,1),(20,'2024-12-04',2,2,1),(21,'2024-12-04',1,2,2),(22,'2024-12-04',2,2,2),(23,'2024-12-04',1,2,3),(24,'2024-12-04',2,2,3),(25,'2024-12-04',1,3,1),(26,'2024-12-04',2,3,1),(27,'2024-12-04',1,3,2),(28,'2024-12-04',2,3,2),(29,'2024-12-04',1,3,3),(30,'2024-12-04',2,3,3),(31,'2024-12-05',1,1,1),(32,'2024-12-05',2,1,1),(33,'2024-12-05',1,1,2),(34,'2024-12-05',2,1,2),(35,'2024-12-05',1,1,3),(36,'2024-12-05',2,1,3),(37,'2024-12-05',1,2,1),(38,'2024-12-05',2,2,1),(39,'2024-12-05',1,2,2),(40,'2024-12-05',2,2,2),(41,'2024-12-05',1,2,3),(42,'2024-12-05',2,2,3),(43,'2024-12-05',1,3,1),(44,'2024-12-05',2,3,1),(45,'2024-12-05',1,3,2),(46,'2024-12-05',2,3,2),(47,'2024-12-05',1,3,3),(48,'2024-12-05',2,3,3);
/*!40000 ALTER TABLE `showtimes` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-27 20:46:41
