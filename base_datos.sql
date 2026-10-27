-- MySQL dump 10.13  Distrib 8.4.11, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: veterinaria_db
-- ------------------------------------------------------
-- Server version	8.4.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado` varchar(20) NOT NULL,
  `fecha_hora` datetime(6) NOT NULL,
  `motivo` varchar(250) NOT NULL,
  `mascota_id` bigint NOT NULL,
  `veterinario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmoh4y7wq0bqnr9hf70j2m8hjk` (`mascota_id`),
  KEY `FKdl5kdl215xbn4q0w55ndq09sm` (`veterinario_id`),
  CONSTRAINT `FKdl5kdl215xbn4q0w55ndq09sm` FOREIGN KEY (`veterinario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKmoh4y7wq0bqnr9hf70j2m8hjk` FOREIGN KEY (`mascota_id`) REFERENCES `mascotas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citas`
--

LOCK TABLES `citas` WRITE;
/*!40000 ALTER TABLE `citas` DISABLE KEYS */;
INSERT INTO `citas` VALUES (1,'Pendiente','2026-10-28 10:00:00.000000','Presenta Vomito y no quiere comer',1,4);
/*!40000 ALTER TABLE `citas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosticos`
--

DROP TABLE IF EXISTS `diagnosticos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diagnosticos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(500) NOT NULL,
  `fecha` datetime(6) NOT NULL,
  `tratamiento` varchar(500) DEFAULT NULL,
  `mascota_id` bigint NOT NULL,
  `veterinario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5r3a4to3omuv9actvqcmhrcaq` (`mascota_id`),
  KEY `FKsbq39m62nyhrwd36xrqm1vql9` (`veterinario_id`),
  CONSTRAINT `FK5r3a4to3omuv9actvqcmhrcaq` FOREIGN KEY (`mascota_id`) REFERENCES `mascotas` (`id`),
  CONSTRAINT `FKsbq39m62nyhrwd36xrqm1vql9` FOREIGN KEY (`veterinario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosticos`
--

LOCK TABLES `diagnosticos` WRITE;
/*!40000 ALTER TABLE `diagnosticos` DISABLE KEYS */;
/*!40000 ALTER TABLE `diagnosticos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mascotas`
--

DROP TABLE IF EXISTS `mascotas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mascotas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `especie` varchar(50) NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `raza` varchar(80) DEFAULT NULL,
  `sexo` varchar(20) DEFAULT NULL,
  `propietario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKadvbg4dgphme4vaxwcqfif3kp` (`propietario_id`),
  CONSTRAINT `FKadvbg4dgphme4vaxwcqfif3kp` FOREIGN KEY (`propietario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mascotas`
--

LOCK TABLES `mascotas` WRITE;
/*!40000 ALTER TABLE `mascotas` DISABLE KEYS */;
INSERT INTO `mascotas` VALUES (1,'Gato','2025-09-19','Copito','criollo','Macho',5),(2,'tortuga','2024-08-24','samy','terrestre','Hembra',5);
/*!40000 ALTER TABLE `mascotas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `correo` varchar(120) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKcdmw5hxlfj78uf4997i3qyyw5` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'admin@veterinaria.com','Administrador','$2a$10$Dn/yIgUdxbRgrb6jsEkwUeOI5HoBhNMFoo6fKljVe27h4tfJzMt6W','ADMIN'),(2,'luisd1@gmail.com','Luis Diaz','$2a$10$tnNCSirf5MNUVXTamlyxAOHTIpSs9GyvRsLBQI7e3hIiOzjciJBzK','PROPIETARIO'),(3,'admin1@veterinaria.com','Admin Aleja','$2a$10$yFjGjSvhUl0muXzkfk2SxeKNyYnxUVdtt.3L7kMlRFS4iUhuu0c7C','PROPIETARIO'),(4,'sarag@veterinaria.com','Sara Guitierrez','$2a$10$qBITqB6jbsLGYQb7kmeLreMOZceu3Oz9aaXEohx6UhjsYZdexaZde','VETERINARIO'),(5,'luis1@veterinaria.com','Luis Diaz','$2a$10$SDjbHi4i7XMwMTWFquRUPuriRXm93dzPZoxlYLfRHREfsFyHOyhnK','PROPIETARIO');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vacunas`
--

DROP TABLE IF EXISTS `vacunas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vacunas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_aplicacion` date NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `proxima_dosis` date DEFAULT NULL,
  `mascota_id` bigint NOT NULL,
  `veterinario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqold5s85s9okwk886lfehm6ds` (`mascota_id`),
  KEY `FKau30ff8d06j3s7k2kdqj3gevy` (`veterinario_id`),
  CONSTRAINT `FKau30ff8d06j3s7k2kdqj3gevy` FOREIGN KEY (`veterinario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKqold5s85s9okwk886lfehm6ds` FOREIGN KEY (`mascota_id`) REFERENCES `mascotas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vacunas`
--

LOCK TABLES `vacunas` WRITE;
/*!40000 ALTER TABLE `vacunas` DISABLE KEYS */;
/*!40000 ALTER TABLE `vacunas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-27 10:17:50
