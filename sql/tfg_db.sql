-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (arm64)
--
-- Host: localhost    Database: tfg_db
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `actividad_ninos`
--

DROP TABLE IF EXISTS `actividad_ninos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actividad_ninos` (
  `actividad_id` bigint NOT NULL,
  `fecha_registro` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nino_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKjt3rys87jh8wc8mj7ga4pefl9` (`nino_id`,`actividad_id`),
  KEY `FKidwm5hefr77dfw83cd1a7gs43` (`actividad_id`),
  CONSTRAINT `FKidwm5hefr77dfw83cd1a7gs43` FOREIGN KEY (`actividad_id`) REFERENCES `actividades` (`actividad_id`),
  CONSTRAINT `FKpkpq0xtdqco3m3snq48gh13ta` FOREIGN KEY (`nino_id`) REFERENCES `ninos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad_ninos`
--

LOCK TABLES `actividad_ninos` WRITE;
/*!40000 ALTER TABLE `actividad_ninos` DISABLE KEYS */;
INSERT INTO `actividad_ninos` VALUES (27,'2025-05-08 15:23:30.746611',36,1),(27,'2025-05-08 15:23:30.765953',37,2);
/*!40000 ALTER TABLE `actividad_ninos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actividades`
--

DROP TABLE IF EXISTS `actividades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actividades` (
  `actividad_id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`actividad_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividades`
--

LOCK TABLES `actividades` WRITE;
/*!40000 ALTER TABLE `actividades` DISABLE KEYS */;
INSERT INTO `actividades` VALUES (17,'Aprender colores basicos','Colores basicos'),(19,'Aprender a leer las vocales','Vocales'),(27,'lectura peter pan','Lectura');
/*!40000 ALTER TABLE `actividades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKhjrv95ryqymlav9uct846evsa` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asistencia` (
  `educador_entrega_id` bigint DEFAULT NULL,
  `educador_recibe_id` bigint NOT NULL,
  `hora_entrada` datetime(6) NOT NULL,
  `hora_salida` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nino_id` bigint NOT NULL,
  `padre_entrega_id` bigint NOT NULL,
  `padre_recoge_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKij6afqafncy95blrr5l64aok7` (`educador_entrega_id`),
  KEY `FKhmgvmqo8qo4y9es0gfm8ryd76` (`educador_recibe_id`),
  KEY `FK4guhj6ui0oqiwn02v166ph3l3` (`nino_id`),
  KEY `FK9xr614mhrpj6hd8d33t684pg9` (`padre_entrega_id`),
  KEY `FKee9y03kv3jt66l29xsudl97cq` (`padre_recoge_id`),
  CONSTRAINT `FK4guhj6ui0oqiwn02v166ph3l3` FOREIGN KEY (`nino_id`) REFERENCES `ninos` (`id`),
  CONSTRAINT `FK9xr614mhrpj6hd8d33t684pg9` FOREIGN KEY (`padre_entrega_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKee9y03kv3jt66l29xsudl97cq` FOREIGN KEY (`padre_recoge_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKhmgvmqo8qo4y9es0gfm8ryd76` FOREIGN KEY (`educador_recibe_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKij6afqafncy95blrr5l64aok7` FOREIGN KEY (`educador_entrega_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
INSERT INTO `asistencia` VALUES (2,2,'2025-03-19 08:00:00.000000','2025-03-19 12:00:00.000000',1,1,3,3),(2,2,'2025-03-19 14:00:00.000000','2025-03-19 18:00:00.000000',3,1,3,3),(2,2,'2025-05-06 12:12:43.000000','2025-05-06 15:12:49.467000',5,1,1,1),(2,5,'2025-05-06 12:20:36.000000','2025-05-06 13:20:42.490000',7,1,1,1),(2,2,'2025-05-06 12:34:50.000000','2025-05-06 14:34:55.793000',8,1,1,1);
/*!40000 ALTER TABLE `asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clase`
--

DROP TABLE IF EXISTS `clase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clase` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `educador_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_clase_educador` (`educador_id`),
  CONSTRAINT `fk_clase_educador` FOREIGN KEY (`educador_id`) REFERENCES `educador` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clase`
--

LOCK TABLES `clase` WRITE;
/*!40000 ALTER TABLE `clase` DISABLE KEYS */;
INSERT INTO `clase` VALUES (1,'Nueva Clase Piojos',2),(2,'Clase B',5);
/*!40000 ALTER TABLE `clase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comidas`
--

DROP TABLE IF EXISTS `comidas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comidas` (
  `educador_id` bigint NOT NULL,
  `hora_comida` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nino_id` bigint NOT NULL,
  `descripcion_comida` varchar(255) NOT NULL,
  `observaciones` text,
  PRIMARY KEY (`id`),
  KEY `FKjksqdqq8fecp1211sw61vh449` (`educador_id`),
  KEY `FK3oojghfsghqjgw935bhy3l3u2` (`nino_id`),
  CONSTRAINT `FK3oojghfsghqjgw935bhy3l3u2` FOREIGN KEY (`nino_id`) REFERENCES `ninos` (`id`),
  CONSTRAINT `FKjksqdqq8fecp1211sw61vh449` FOREIGN KEY (`educador_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comidas`
--

LOCK TABLES `comidas` WRITE;
/*!40000 ALTER TABLE `comidas` DISABLE KEYS */;
INSERT INTO `comidas` VALUES (2,'2025-05-05 10:45:42.000000',1,1,'Puré de patatas y zanahoria','Comió todo sin problemas.'),(2,'2025-03-19 12:30:00.000000',5,1,'Puré de verduras','Comió poco.'),(2,'2025-03-19 16:30:00.000000',6,1,'Filetes de pollo','Comió poco.'),(2,'2025-03-19 18:30:00.000000',7,1,'Filetes de ternera con patatas','Comió muy bien.'),(5,'2025-05-05 08:29:00.000000',8,6,'Fruta y cerales con leche','Muy bien'),(2,'2025-05-05 09:30:00.000000',11,2,'Cereales con leche','Muy bien');
/*!40000 ALTER TABLE `comidas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `educador`
--

DROP TABLE IF EXISTS `educador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `educador` (
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKehr2jtv97sks23oq5hdhpeatg` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `educador`
--

LOCK TABLES `educador` WRITE;
/*!40000 ALTER TABLE `educador` DISABLE KEYS */;
INSERT INTO `educador` VALUES (2),(5);
/*!40000 ALTER TABLE `educador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evento_ninos`
--

DROP TABLE IF EXISTS `evento_ninos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evento_ninos` (
  `evento_id` bigint NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nino_id` bigint NOT NULL,
  `asistio` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6ejv6gufgiljsf35mnjm7t7gb` (`evento_id`),
  KEY `FKgcfuhbsfdvjy46rghx1dg72wj` (`nino_id`),
  CONSTRAINT `FK6ejv6gufgiljsf35mnjm7t7gb` FOREIGN KEY (`evento_id`) REFERENCES `eventos` (`id`),
  CONSTRAINT `FKgcfuhbsfdvjy46rghx1dg72wj` FOREIGN KEY (`nino_id`) REFERENCES `ninos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evento_ninos`
--

LOCK TABLES `evento_ninos` WRITE;
/*!40000 ALTER TABLE `evento_ninos` DISABLE KEYS */;
INSERT INTO `evento_ninos` VALUES (2,1,5,_binary '');
/*!40000 ALTER TABLE `evento_ninos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `eventos`
--

DROP TABLE IF EXISTS `eventos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventos` (
  `creador_id` bigint NOT NULL,
  `fecha_hora` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` text,
  `titulo` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK89bwry63q72x71i9ry8ct3gn4` (`creador_id`),
  CONSTRAINT `FK89bwry63q72x71i9ry8ct3gn4` FOREIGN KEY (`creador_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventos`
--

LOCK TABLES `eventos` WRITE;
/*!40000 ALTER TABLE `eventos` DISABLE KEYS */;
INSERT INTO `eventos` VALUES (2,'2025-03-20 10:30:00.291653',1,NULL,'Taller de Pintura'),(2,'2025-03-19 10:00:00.000000',2,'evento de manualidades con plastilina','Manualidades');
/*!40000 ALTER TABLE `eventos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `higiene`
--

DROP TABLE IF EXISTS `higiene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `higiene` (
  `educador_id` bigint NOT NULL,
  `fecha_hora` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nino_id` bigint NOT NULL,
  `observaciones` text,
  `estado` enum('ESTREÑIDO','NORMAL','SUELTO') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKamk5vlv1jfvn378j6vye816vs` (`educador_id`),
  KEY `FKsybo0kpmfk1tg5ot1aun0nrn0` (`nino_id`),
  CONSTRAINT `FKamk5vlv1jfvn378j6vye816vs` FOREIGN KEY (`educador_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKsybo0kpmfk1tg5ot1aun0nrn0` FOREIGN KEY (`nino_id`) REFERENCES `ninos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `higiene`
--

LOCK TABLES `higiene` WRITE;
/*!40000 ALTER TABLE `higiene` DISABLE KEYS */;
INSERT INTO `higiene` VALUES (2,'2025-03-19 18:30:00.000000',2,2,'Todo correcto, sin incidencias','NORMAL'),(2,'2025-03-19 20:30:00.000000',3,2,'Poco y mal','ESTREÑIDO'),(2,'2025-03-21 10:30:00.000000',4,1,'Se cambió el pañal sin incidencias.','NORMAL');
/*!40000 ALTER TABLE `higiene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mensajes`
--

DROP TABLE IF EXISTS `mensajes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mensajes` (
  `emisor_id` bigint NOT NULL,
  `fecha_hora` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `receptor_id` bigint NOT NULL,
  `contenido` text NOT NULL,
  `estado` enum('LEIDO','NO_LEIDO') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmg4oe68p9t40440gdttlg0fny` (`emisor_id`),
  KEY `FK828al04b97o1tjvlhfhn3eici` (`receptor_id`),
  CONSTRAINT `FK828al04b97o1tjvlhfhn3eici` FOREIGN KEY (`receptor_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FKmg4oe68p9t40440gdttlg0fny` FOREIGN KEY (`emisor_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensajes`
--

LOCK TABLES `mensajes` WRITE;
/*!40000 ALTER TABLE `mensajes` DISABLE KEYS */;
INSERT INTO `mensajes` VALUES (1,'2025-03-24 07:30:05.809638',1,2,'Hola, ¿cómo está hoy mi hijo?','LEIDO'),(2,'2025-03-24 07:38:38.030319',2,1,'Hola, bien se esta portando muy bien y esta jugando mucho con los demás.','LEIDO'),(1,'2025-03-24 08:18:09.271515',3,2,'Perfecto, voy a recogerle a las 14h, gracias.','NO_LEIDO');
/*!40000 ALTER TABLE `mensajes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ninos`
--

DROP TABLE IF EXISTS `ninos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ninos` (
  `fecha_nacimiento` date NOT NULL,
  `primer_dia` date NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `alergias` varchar(255) DEFAULT NULL,
  `apellidos` varchar(255) NOT NULL,
  `condiciones_medicas` varchar(255) DEFAULT NULL,
  `foto_url` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) NOT NULL,
  `clase_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKh9j1v0vb7hfgypo6dcbg8aqsi` (`clase_id`),
  CONSTRAINT `FKh9j1v0vb7hfgypo6dcbg8aqsi` FOREIGN KEY (`clase_id`) REFERENCES `clase` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ninos`
--

LOCK TABLES `ninos` WRITE;
/*!40000 ALTER TABLE `ninos` DISABLE KEYS */;
INSERT INTO `ninos` VALUES ('2020-05-15','2025-02-08',1,'Polen','Martinez Lopez','Asma','https://ejemplo.com/foto.jpg','Carlitos',1),('2020-05-15','2025-02-08',2,'','Lopez Zapata','',NULL,'Antonio',1),('2021-10-15','2025-03-18',5,'Ninguna','Ramírez Pérez','Ninguna','https://ejemplo.com/foto_sofia.jpg','Sofía',2),('2000-04-02','2025-04-05',6,'','Ramírez Pérez','','','Ruben',NULL);
/*!40000 ALTER TABLE `ninos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificaciones`
--

DROP TABLE IF EXISTS `notificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificaciones` (
  `emisor_id` bigint NOT NULL,
  `fecha_hora` datetime(6) NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `receptor_id` bigint NOT NULL,
  `estado` enum('LEIDO','NO_LEIDO') NOT NULL,
  `mensaje` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK93ejqhq3va6nhwfnpat6mxl52` (`emisor_id`),
  KEY `FK85l9q005gqdurek2lnal2c7rs` (`receptor_id`),
  CONSTRAINT `FK85l9q005gqdurek2lnal2c7rs` FOREIGN KEY (`receptor_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK93ejqhq3va6nhwfnpat6mxl52` FOREIGN KEY (`emisor_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificaciones`
--

LOCK TABLES `notificaciones` WRITE;
/*!40000 ALTER TABLE `notificaciones` DISABLE KEYS */;
INSERT INTO `notificaciones` VALUES (2,'2025-03-20 13:23:13.016552',1,1,'LEIDO','Tu hijo/a null ha comido: Puré de verduras Observaciones: Comió poco.'),(2,'2025-03-20 13:30:08.649508',2,1,'LEIDO','Tu hijo/a su hijo/a ha comido: Filetes de pollo Observaciones: Comió poco.'),(2,'2025-03-20 19:44:55.258378',3,1,'LEIDO','Tu hijo/a ha comido: Filetes de ternera Observaciones: Comió muy bien.'),(2,'2025-03-20 22:11:11.589350',4,1,'NO_LEIDO','Tu hijo/a ha iniciado una siesta a las 2025-03-20T13:00 y se despertó a las 2025-03-20T14:30.'),(2,'2025-03-20 22:15:04.152367',5,1,'NO_LEIDO','Tu hijo/a ha iniciado una siesta a las 2025-03-20T15:00 y se despertó a las 2025-03-20T15:30. Duración: 30 minutos. Observaciones: Durmió poco y mal.'),(2,'2025-03-20 22:24:23.935025',6,1,'NO_LEIDO','Tu hijo/a ha realizado una higiene: NORMAL Observaciones: Se cambió el pañal sin incidencias.'),(5,'2025-03-20 22:34:31.680991',7,1,'NO_LEIDO','Tu hijo/a Antonio ha sido asignado a la clase Nueva Clase A con el educador Laura.'),(1,'2025-03-24 07:30:05.809850',8,2,'NO_LEIDO','Tienes un nuevo mensaje de null'),(2,'2025-03-24 07:38:38.030361',9,1,'NO_LEIDO','Tienes un nuevo mensaje de null'),(1,'2025-03-24 08:18:09.275980',11,2,'NO_LEIDO','Tienes un nuevo mensaje de Victor'),(5,'2025-03-24 19:26:17.164249',12,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Explorar los sentidos'),(5,'2025-03-24 19:26:17.174548',13,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Explorar los sentidos'),(2,'2025-05-05 16:48:46.623149',14,1,'NO_LEIDO','Tu hijo/a ha comido: Puré de patatas y zanahoria Observaciones: Comió todo sin problemas.'),(2,'2025-05-05 16:50:57.731176',15,1,'NO_LEIDO','Tu hijo/a ha comido: Puré de patatas y zanahoria Observaciones: Comió todo sin problemas.'),(2,'2025-05-05 17:18:12.908500',16,1,'NO_LEIDO','Tu hijo/a ha comido: Cereales con leche'),(2,'2025-05-06 16:30:52.257591',17,1,'NO_LEIDO','Tu hijo/a ha realizado una higiene: SUELTO'),(2,'2025-05-06 16:57:28.400358',18,1,'NO_LEIDO','Tu hijo/a ha iniciado una siesta a las 2025-05-06T16:40:52.472 y se despertó a las 2025-05-06T16:54:56.489. Duración: 14 minutos. Observaciones: Sin observaciones'),(2,'2025-05-06 16:57:42.378433',19,1,'NO_LEIDO','Tu hijo/a ha iniciado una siesta a las 2025-05-06T16:30:52.472 y se despertó a las 2025-05-06T16:54:56.489. Duración: 24 minutos. Observaciones: Sin observaciones'),(2,'2025-05-07 12:43:35.679060',20,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:35.697361',21,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:36.964817',22,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:36.995485',23,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.179422',24,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.196032',25,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.358951',26,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.376432',27,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.513852',28,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.538673',29,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.837324',30,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:37.864657',31,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.030590',32,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.062865',33,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.184864',34,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.194602',35,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.329553',36,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.345203',37,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.538133',38,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:43:38.562509',39,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Ejemplo'),(2,'2025-05-07 12:48:09.484189',40,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: a'),(2,'2025-05-07 12:48:09.506677',41,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: a'),(2,'2025-05-07 12:49:25.170556',42,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: aaa'),(2,'2025-05-07 12:49:25.183769',43,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: aaa'),(2,'2025-05-07 12:56:34.572627',44,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: bbbb'),(2,'2025-05-07 12:56:34.582566',45,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: bbbb'),(2,'2025-05-08 10:16:16.107706',46,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: ApiDog'),(2,'2025-05-08 13:27:21.909552',47,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: 24'),(2,'2025-05-08 13:27:21.919576',48,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: 24'),(2,'2025-05-08 15:23:30.758314',49,1,'NO_LEIDO','Tu hijo/a Carlitos ha participado en la actividad: Lectura'),(2,'2025-05-08 15:23:30.773592',50,1,'NO_LEIDO','Tu hijo/a Antonio ha participado en la actividad: Lectura');
/*!40000 ALTER TABLE `notificaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `padre`
--

DROP TABLE IF EXISTS `padre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `padre` (
  `id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKbpqtd09e9r032bwy2eued79xw` FOREIGN KEY (`id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `padre`
--

LOCK TABLES `padre` WRITE;
/*!40000 ALTER TABLE `padre` DISABLE KEYS */;
INSERT INTO `padre` VALUES (1),(3);
/*!40000 ALTER TABLE `padre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `padres_hijos`
--

DROP TABLE IF EXISTS `padres_hijos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `padres_hijos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nino_id` bigint NOT NULL,
  `padre_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKe7f74gofmscl7x5g2q5s533mc` (`nino_id`),
  KEY `FK58im1fgm5ltst8ta511odi9qs` (`padre_id`),
  CONSTRAINT `FK58im1fgm5ltst8ta511odi9qs` FOREIGN KEY (`padre_id`) REFERENCES `padre` (`id`),
  CONSTRAINT `FKe7f74gofmscl7x5g2q5s533mc` FOREIGN KEY (`nino_id`) REFERENCES `ninos` (`id`),
  CONSTRAINT `FKpq2gg2ocnph8rlewdhc0plpj6` FOREIGN KEY (`padre_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `padres_hijos`
--

LOCK TABLES `padres_hijos` WRITE;
/*!40000 ALTER TABLE `padres_hijos` DISABLE KEYS */;
INSERT INTO `padres_hijos` VALUES (1,1,1),(2,2,1);
/*!40000 ALTER TABLE `padres_hijos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `siestas`
--

DROP TABLE IF EXISTS `siestas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `siestas` (
  `educador_id` bigint NOT NULL,
  `fin_siesta` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `inicio_siesta` datetime(6) NOT NULL,
  `nino_id` bigint NOT NULL,
  `observaciones` text,
  PRIMARY KEY (`id`),
  KEY `FKp4ds8n9gbmafp93hfjl4nvgr` (`educador_id`),
  KEY `FK1wywruhdqbwmkwiovw5d4ft8f` (`nino_id`),
  CONSTRAINT `FK1wywruhdqbwmkwiovw5d4ft8f` FOREIGN KEY (`nino_id`) REFERENCES `ninos` (`id`),
  CONSTRAINT `FKp4ds8n9gbmafp93hfjl4nvgr` FOREIGN KEY (`educador_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `siestas`
--

LOCK TABLES `siestas` WRITE;
/*!40000 ALTER TABLE `siestas` DISABLE KEYS */;
INSERT INTO `siestas` VALUES (2,'2025-03-19 15:30:00.000000',1,'2025-03-19 14:00:00.000000',1,'Durmió profundamente.'),(2,'2025-03-20 17:00:00.000000',4,'2025-03-19 16:30:00.000000',1,'Durmió profundamente.'),(2,'2025-03-20 14:30:00.000000',5,'2025-03-20 13:00:00.000000',1,'Durmió profundamente.'),(2,'2025-03-20 15:30:00.000000',6,'2025-03-20 15:00:00.000000',1,'Durmió poco y mal.'),(2,'2025-05-06 16:54:56.489000',7,'2025-05-06 16:40:52.472000',1,'Sin observaciones');
/*!40000 ALTER TABLE `siestas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `telefono` varchar(255) NOT NULL,
  `tipo_usuario` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkfsp0s1tflm1cwlj8idhqsad0` (`email`),
  UNIQUE KEY `UK7a6nioycwi246dw0dn2etj3ye` (`telefono`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Lopez Lopez','victorlopez@pruebas.com','Victor','$2a$10$l5KSnSSCEjFAG.CLUsJyse4dsRYZIqzovsawVHBJo5qwzbSegZ0lq','123456789','PADRE'),(2,'Zapata Mora','lorenazapata@pruebas.com','Lorena','$2a$10$3J.K1XHAFMm4u01sXDguoOA0xoJhN6v.e4vPWCTn1HZsmWldjQZ8u','987654321','EDUCADOR'),(3,'Gómez Ruiz','carlosgomez@pruebas.com','Carlos','$2a$10$DuFN6icgvv7m9GN27wTA..qMMHvBFVV/NPY4L5V7u9qhO7.0D261K','666777888','PADRE'),(5,'López López','laurafernandez@guarderia.com','Laura','$2a$10$snv297wolA58TRusJEJkE.2KdTJTkaqx9PeHCp.t80dQYuiGhDP3K','654321987','EDUCADOR');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-12 22:11:01
