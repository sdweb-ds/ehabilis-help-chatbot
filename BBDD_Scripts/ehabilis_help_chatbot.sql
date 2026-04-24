-- MySQL dump
--
-- Host:    Database: ehabilis_help_chatbot
-- ------------------------------------------------------

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
-- Table structure for table `constante`
--

DROP TABLE IF EXISTS `constante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `constante` (
  `id_constante` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `valor` varchar(1024) DEFAULT NULL,
  `descripcion` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id_constante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `constante`
--

LOCK TABLES `constante` WRITE;
/*!40000 ALTER TABLE `constante` DISABLE KEYS */;
INSERT INTO `constante` VALUES (34,'MODO.DEBUG','1','INDICA SI LA APLICACION SE ENCUENTRA EN MODO DEBUG (1: SI, 0: NO)');
/*!40000 ALTER TABLE `constante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dato_elemento`
--

DROP TABLE IF EXISTS `dato_elemento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dato_elemento` (
  `codElemento` varchar(255) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `ubicacion` varchar(255) DEFAULT NULL,
  `url` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`codElemento`),
  CONSTRAINT `c_dato_elemento_dato` FOREIGN KEY (`codElemento`) REFERENCES `elemento` (`codElemento`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dato_elemento`
--

LOCK TABLES `dato_elemento` WRITE;
/*!40000 ALTER TABLE `dato_elemento` DISABLE KEYS */;
INSERT INTO `dato_elemento` VALUES ('M.0','menu.gestion.usuarios','M.0 descripcion','M','98','/BusquedaUsuario.do?accion=cargar'),('M.0.1','menu.mantenimiento.usuarios','M.0.1 descripcion','M','98.1','/BusquedaUsuario.do?accion=cargar'),('M.0.1.1','menu.buscar','M.0.1.1 descripcion','M','98.1.1','/BusquedaUsuario.do?accion=cargar'),('M.0.1.2','menu.crear','M.0.1.2 descripcion','M','98.1.2','/CrearModificarUsuario.do?accion=cargar'),('M.0.2','menu.mantenimiento.perfiles','M.0.2 descripcion','M','98.2','/BusquedaPerfil.do?accion=cargar'),('M.0.2.1','menu.buscar','M.0.2.1 descripcion','M','98.2.1','/BusquedaPerfil.do?accion=cargar'),('M.0.2.2','menu.crear','M.0.2.2 descripcion','M','98.2.2','/CrearModificarPerfil.do?accion=cargar'),('M.1','menu.cargar.frase','M.1 descripcion','M','1','/CargarFrase.do?accion=cargar'),('M.10','menu.resetear.memoria','M.10 descripcion','M','9','/ResetearMemoria.do?accion=cargar'),('M.11','menu.obtener.red','M.11 descripcion','M','10','/ObtenerRed.do?accion=cargar'),('M.12','menu.comprobar.verbo','M.12 descripcion','M','4','/ComprobarVerbo.do?accion=cargar'),('M.13','menu.obtener.cualidades','M.13 descripcion','M','6','/ObtenerCualidades.do?accion=cargar'),('M.14','menu.obtener.nandas','M.14 descripcion','M','2','/ObtenerNandas.do?accion=cargar'),('M.15','menu.obtener.nics','M.15 descripcion','M','3','/ObtenerNics.do?accion=cargar'),('M.16','menu.obtener.nocs','M.16 descripcion','M','4','/ObtenerNocs.do?accion=cargar'),('M.17','menu.procesar.texto','M.17 descripcion','M','17','/ProcesarTexto.do?accion=cargar'),('M.18','menu.funcionalidades.nlp','M.18 descripcion','M','1','/'),('M.19','menu.funcionalidades.nlg','M.19 descripcion','M','2','/'),('M.2','menu.analizar.estructura','M.2 descripcion','M','1','/AnalizarEstructura.do?accion=cargar'),('M.20','menu.funcionalidades.nlu','M.20 descripcion','M','3','/'),('M.21','menu.ontologias.sanitarias','M.21 descripcion','M','4','/'),('M.22','menu.chatbot.memorycorp','M.22 descripcion','M','5','/Chat.do?accion=cargar'),('M.23','menu.verificar.ner','M.23 descripcion','M','5','/VerificarNer.do?accion=cargar'),('M.24','menu.cargar.conjunto.reglas.nlp.nlg','M.24 descripcion','M','1','/CargarReglas.do?accion=cargar'),('M.25','menu.visualizar.reglas.nlp.nlg','M.25 descripcion','M','2','/ObtenerReglas.do?accion=cargar'),('M.26','menu.enviar.frase.chatbot','M.26 descripcion','M','2','/EnviarFraseChatBot.do?accion=cargar'),('M.27','menu.ontologia.snomed.ct','M.27 descripcion','M','1','/ObtenerOntologiaSnomed.do?accion=cargar'),('M.28','menu.acerca.de','M.28 descripcion','M','99','/pages/static/acerca_de.jsp'),('M.3','menu.obtener.sinonimos','M.3 descripcion','M','2','/ObtenerSinonimos.do?accion=cargar'),('M.4','menu.obtener.antonimos','M.4 descripcion','M','3','/ObtenerAntonimos.do?accion=cargar'),('M.5','menu.evaluar.frase','M.5 descripcion','M','3','/EvaluarFrase.do?accion=cargar'),('M.6','menu.obtener.componentes','M.6 descripcion','M','4','/ObtenerComponentes.do?accion=cargar'),('M.7','menu.obtener.agregadores','M.7 descripcion','M','5','/ObtenerAgregadores.do?accion=cargar'),('M.8','menu.obtener.subclases','M.8 descripcion','M','7','/ObtenerSubclases.do?accion=cargar'),('M.9','menu.obtener.superclases','M.9 descripcion','M','8','/ObtenerSuperclases.do?accion=cargar');
/*!40000 ALTER TABLE `dato_elemento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elemento`
--

DROP TABLE IF EXISTS `elemento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elemento` (
  `codElemento` varchar(255) NOT NULL,
  `codelepadre` varchar(255) DEFAULT NULL,
  `deselemento` varchar(255) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `visible` int(11) DEFAULT '1',
  PRIMARY KEY (`codElemento`),
  KEY `codelepadre` (`codelepadre`),
  CONSTRAINT `elemento_ibfk_1` FOREIGN KEY (`codelepadre`) REFERENCES `elemento` (`codElemento`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elemento`
--

LOCK TABLES `elemento` WRITE;
/*!40000 ALTER TABLE `elemento` DISABLE KEYS */;
INSERT INTO `elemento` VALUES ('M.0',NULL,'Permiso de acceso al menu Gestión usuarios.',98,1),('M.0.1','M.0','Permiso de acceso al menu Gestión usuarios > Usuario.',NULL,1),('M.0.1.1','M.0.1','Permiso de acceso al menu Gestión usuarios > Usuario > Buscar.',NULL,1),('M.0.1.2','M.0.1','Permiso de acceso al menu Gestión usuarios > Usuario > Crear.',NULL,1),('M.0.2','M.0','Permiso de acceso al menu Gestión usuarios > Perfiles.',NULL,1),('M.0.2.1','M.0.2','Permiso de acceso al menu Gestión usuarios > Perfiles > Buscar.',NULL,1),('M.0.2.2','M.0.2','Permiso de acceso al menu Gestión usuarios > Perfiles > Crear.',NULL,1),('M.1','M.20','Permiso de acceso al menu Cargar Frase.',1,1),('M.10','M.20','Permiso de acceso al menu Resetear memoria.',9,1),('M.11','M.20','Permiso de acceso al menu Obtener red.',10,1),('M.12','M.18','Permiso de acceso al menu Comprobar verbo.',4,1),('M.13','M.20','Permiso de acceso al menu Obtener cualidades.',6,1),('M.14','M.21','Permiso de acceso al menu Obtener nandas.',2,1),('M.15','M.21','Permiso de acceso al menu Obtener nics.',3,1),('M.16','M.21','Permiso de acceso al menu Obtener nocs.',4,1),('M.17',NULL,'Permiso de acceso al menu Procesar texto.',17,1),('M.18',NULL,'Permiso de acceso al menu Funcionalidades NLP.',1,1),('M.19',NULL,'Permiso de acceso al menu Funcionalidades NLG.',2,1),('M.2','M.18','Permiso de acceso al menu Analizar estructura.',1,1),('M.20',NULL,'Permiso de acceso al menu Funcionalidades NLU.',3,1),('M.21',NULL,'Permiso de acceso al menu Ontologías Sanitarias.',4,1),('M.22',NULL,'Permiso de acceso al menu ChatBot MemoryCorp.',5,1),('M.23','M.18','Permiso de acceso al menu Verificar NER.',5,1),('M.24','M.19','Permiso de acceso al menu Cargar Conjunto de Reglas NLP/NLG.',1,1),('M.25','M.19','Permiso de acceso al menu Visualizar Conjunto de Reglas NLP/NLG.',2,1),('M.26','M.20','Permiso de acceso al menu Enviar frase al ChatBot.',2,1),('M.27','M.21','Permiso de acceso al menu Ontología SNOMED-CT.',1,1),('M.28',NULL,'Permiso de acceso al menu Acerca de.',99,1),('M.3','M.18','Permiso de acceso al menu Obtener sinónimos.',2,1),('M.4','M.18','Permiso de acceso al menu Obtener antónimos.',3,1),('M.5','M.20','Permiso de acceso al menu Evaluar frase.',3,1),('M.6','M.20','Permiso de acceso al menu Obtener componentes.',4,1),('M.7','M.20','Permiso de acceso al menu Obtener agregadores.',5,1),('M.8','M.20','Permiso de acceso al menu Obtener subclases.',7,1),('M.9','M.20','Permiso de acceso al menu Obtener superclases.',8,1);
/*!40000 ALTER TABLE `elemento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elemento_perfil`
--

DROP TABLE IF EXISTS `elemento_perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elemento_perfil` (
  `codElemento` varchar(255) DEFAULT NULL,
  `codPerfil` varchar(255) DEFAULT NULL,
  KEY `codElemento` (`codElemento`),
  KEY `codPerfil` (`codPerfil`),
  CONSTRAINT `elemento_perfil_ibfk_1` FOREIGN KEY (`codElemento`) REFERENCES `elemento` (`codElemento`) ON DELETE CASCADE,
  CONSTRAINT `elemento_perfil_ibfk_2` FOREIGN KEY (`codPerfil`) REFERENCES `perfil` (`codPerfil`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elemento_perfil`
--

LOCK TABLES `elemento_perfil` WRITE;
/*!40000 ALTER TABLE `elemento_perfil` DISABLE KEYS */;
INSERT INTO `elemento_perfil` VALUES ('M.0','ADM'),('M.0.1','ADM'),('M.0.1.1','ADM'),('M.0.1.2','ADM'),('M.0.2','ADM'),('M.0.2.1','ADM'),('M.0.2.2','ADM'),('M.18','ADM'),('M.12','ADM'),('M.23','ADM'),('M.3','ADM'),('M.4','ADM'),('M.19','ADM'),('M.24','ADM'),('M.25','ADM'),('M.20','ADM'),('M.1','ADM'),('M.10','ADM'),('M.11','ADM'),('M.13','ADM'),('M.26','ADM'),('M.5','ADM'),('M.6','ADM'),('M.7','ADM'),('M.8','ADM'),('M.9','ADM'),('M.22','ADM'),('M.2','ADM'),('M.28','ADM');
/*!40000 ALTER TABLE `elemento_perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entidad`
--

DROP TABLE IF EXISTS `entidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entidad` (
  `id_entidad` int(11) NOT NULL,
  `nombre_comercial` varchar(255) DEFAULT NULL,
  `nombre_legal` varchar(255) DEFAULT NULL,
  `cif` varchar(255) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `id_region` int(11) DEFAULT NULL,
  `id_provincia` int(11) DEFAULT NULL,
  `id_localidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_entidad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entidad`
--

LOCK TABLES `entidad` WRITE;
/*!40000 ALTER TABLE `entidad` DISABLE KEYS */;
/*!40000 ALTER TABLE `entidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `localidad`
--

DROP TABLE IF EXISTS `localidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `localidad` (
  `id_localidad` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `id_provincia` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_localidad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `localidad`
--

LOCK TABLES `localidad` WRITE;
/*!40000 ALTER TABLE `localidad` DISABLE KEYS */;
/*!40000 ALTER TABLE `localidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pais`
--

DROP TABLE IF EXISTS `pais`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pais` (
  `id_pais` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `locale` varchar(10) NOT NULL,
  PRIMARY KEY (`id_pais`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pais`
--

LOCK TABLES `pais` WRITE;
/*!40000 ALTER TABLE `pais` DISABLE KEYS */;
/*!40000 ALTER TABLE `pais` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil`
--

DROP TABLE IF EXISTS `perfil`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfil` (
  `codPerfil` varchar(255) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `entidad` int(11) DEFAULT NULL,
  `visible` int(11) DEFAULT '1',
  PRIMARY KEY (`codPerfil`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil`
--

LOCK TABLES `perfil` WRITE;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` VALUES ('ADM','ADMINISTRADOR','PERFIL ADMINISTRADOR',NULL,0);
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `perfil_usuario`
--

DROP TABLE IF EXISTS `perfil_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfil_usuario` (
  `codUsuario` varchar(255) DEFAULT NULL,
  `codPerfil` varchar(255) DEFAULT NULL,
  KEY `codUsuario` (`codUsuario`),
  KEY `codPerfil` (`codPerfil`),
  CONSTRAINT `perfil_usuario_ibfk_1` FOREIGN KEY (`codUsuario`) REFERENCES `usuario` (`codUsuario`) ON DELETE CASCADE,
  CONSTRAINT `perfil_usuario_ibfk_2` FOREIGN KEY (`codPerfil`) REFERENCES `perfil` (`codPerfil`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `perfil_usuario`
--

LOCK TABLES `perfil_usuario` WRITE;
/*!40000 ALTER TABLE `perfil_usuario` DISABLE KEYS */;
INSERT INTO `perfil_usuario` VALUES ('0000000001','ADM');
/*!40000 ALTER TABLE `perfil_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provincia`
--

DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provincia` (
  `id_provincia` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `id_region` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_provincia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provincia`
--

LOCK TABLES `provincia` WRITE;
/*!40000 ALTER TABLE `provincia` DISABLE KEYS */;
/*!40000 ALTER TABLE `provincia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `region` (
  `id_region` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `id_pais` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traduccion`
--

DROP TABLE IF EXISTS `traduccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traduccion` (
  `locale` varchar(255) NOT NULL,
  `clave` varchar(255) NOT NULL,
  `texto` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traduccion`
--

LOCK TABLES `traduccion` WRITE;
/*!40000 ALTER TABLE `traduccion` DISABLE KEYS */;
/*!40000 ALTER TABLE `traduccion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `codUsuario` varchar(255) NOT NULL,
  `idioma` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido1` varchar(255) DEFAULT NULL,
  `apellido2` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `correo_elec` varchar(255) DEFAULT NULL,
  `nif` varchar(255) DEFAULT NULL,
  `id_entidad` int(11) DEFAULT NULL,
  PRIMARY KEY (`codUsuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('0000000001','es_ES','admin','PThj+AqzE9k=','Admin','User',NULL,'881 819 107','ehabilis-help-chatbot.admin@ehabilis.es','00000001Z',NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed
