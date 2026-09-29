-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               12.1.1-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table memedb.category
CREATE TABLE IF NOT EXISTS `category` (
  `cat_ID` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cat_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;

-- Dumping data for table memedb.category: ~2 rows (approximately)
DELETE FROM `category`;
INSERT INTO `category` (`cat_ID`, `category`) VALUES
	(1, 'Kategorie1'),
	(2, 'Kategorie2');

-- Dumping structure for table memedb.memes
CREATE TABLE IF NOT EXISTS `memes` (
  `meme_ID` int(11) NOT NULL AUTO_INCREMENT,
  `pic` blob DEFAULT NULL,
  `date` date DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `tag` int(11) DEFAULT NULL,
  PRIMARY KEY (`meme_ID`) USING BTREE,
  KEY `category` (`category`),
  KEY `tag` (`tag`),
  CONSTRAINT `category` FOREIGN KEY (`category`) REFERENCES `category` (`cat_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `tag` FOREIGN KEY (`tag`) REFERENCES `tag` (`tag_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='Die Memes der Database';

-- Dumping data for table memedb.memes: ~2 rows (approximately)
DELETE FROM `memes`;
INSERT INTO `memes` (`meme_ID`, `pic`, `date`, `height`, `length`, `size`, `category`, `tag`) VALUES
	(1, _binary 0x42696c6431, '2025-11-17', 1024, 1280, 0, NULL, NULL),
	(2, _binary 0x42696c6432, '2025-11-17', 1080, 1920, 0, NULL, NULL);

-- Dumping structure for table memedb.tag
CREATE TABLE IF NOT EXISTS `tag` (
  `tag_ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`tag_ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci COMMENT='tags';

-- Dumping data for table memedb.tag: ~2 rows (approximately)
DELETE FROM `tag`;
INSERT INTO `tag` (`tag_ID`, `name`) VALUES
	(1, 'tag1'),
	(2, 'tag2');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
