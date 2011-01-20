-- MySQL dump 10.13  Distrib 5.1.37, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: toxotis
-- ------------------------------------------------------
-- Server version	5.1.37-1ubuntu5.5

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
-- Table structure for table `Algorithm`
--

DROP TABLE IF EXISTS `Algorithm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Algorithm` (
  `uri` varchar(255) NOT NULL,
  PRIMARY KEY (`uri`),
  KEY `uri_in_algorithm_references_OTComponent` (`uri`),
  CONSTRAINT `uri_in_algorithm_references_OTComponent` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AlgorithmOntol`
--

DROP TABLE IF EXISTS `AlgorithmOntol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AlgorithmOntol` (
  `algorithmUri` varchar(255) NOT NULL,
  `DATATYPE_NS` varchar(255) NOT NULL,
  `DATATYPE_Name` varchar(255) NOT NULL,
  PRIMARY KEY (`algorithmUri`,`DATATYPE_NS`,`DATATYPE_Name`),
  KEY `algorithmOntology` (`DATATYPE_NS`,`DATATYPE_Name`),
  KEY `algorithmUri_ref_for_ontologies` (`algorithmUri`),
  CONSTRAINT `algorithmOntology` FOREIGN KEY (`DATATYPE_NS`, `DATATYPE_Name`) REFERENCES `OntologicalClass` (`nameSpace`, `name`),
  CONSTRAINT `algorithmUri_ref_for_ontologies` FOREIGN KEY (`algorithmUri`) REFERENCES `Algorithm` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AlgorithmParams`
--

DROP TABLE IF EXISTS `AlgorithmParams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AlgorithmParams` (
  `algorithmUri` varchar(255) NOT NULL,
  `parameterUri` varchar(255) NOT NULL,
  PRIMARY KEY (`algorithmUri`,`parameterUri`),
  KEY `algorithmUri_ref_for_Parameters` (`algorithmUri`),
  KEY `parameterUri_ref_for_algorithm` (`parameterUri`),
  CONSTRAINT `algorithmUri_ref_for_Parameters` FOREIGN KEY (`algorithmUri`) REFERENCES `Algorithm` (`uri`),
  CONSTRAINT `parameterUri_ref_for_algorithm` FOREIGN KEY (`parameterUri`) REFERENCES `Parameter` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `BibTeX`
--

DROP TABLE IF EXISTS `BibTeX`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BibTeX` (
  `uri` varchar(255) NOT NULL,
  `abstract` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `annotation` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `bibType` varchar(255) DEFAULT NULL,
  `bookTitle` varchar(255) DEFAULT NULL,
  `chapter` varchar(255) DEFAULT NULL,
  `copyright` varchar(255) DEFAULT NULL,
  `crossref` varchar(255) DEFAULT NULL,
  `edition` varchar(255) DEFAULT NULL,
  `editor` varchar(255) DEFAULT NULL,
  `isbn` varchar(20) DEFAULT NULL,
  `issn` varchar(20) DEFAULT NULL,
  `journal` varchar(255) DEFAULT NULL,
  `bibkey` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `pages` varchar(20) DEFAULT NULL,
  `series` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uri`),
  KEY `FK7695512CB5362BBC` (`uri`),
  KEY `FK7695512CA2493D53` (`createdBy`),
  CONSTRAINT `FK7695512CA2493D53` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`),
  CONSTRAINT `FK7695512CB5362BBC` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Compound`
--

DROP TABLE IF EXISTS `Compound`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Compound` (
  `uri` varchar(255) NOT NULL,
  PRIMARY KEY (`uri`),
  KEY `uri_in_compound` (`uri`),
  CONSTRAINT `uri_in_compound` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DataEntry`
--

DROP TABLE IF EXISTS `DataEntry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DataEntry` (
  `uri` varchar(255) NOT NULL,
  `conformer` varchar(255) DEFAULT NULL,
  `datasetUri` varchar(255) DEFAULT NULL,
  `dataEntryDs_IDX` int(11) DEFAULT NULL,
  PRIMARY KEY (`uri`),
  KEY `FK605882C8A71B5F57` (`conformer`),
  KEY `uri_in_dataEntry` (`uri`),
  KEY `FK605882C8F0840213` (`datasetUri`),
  CONSTRAINT `FK605882C8A71B5F57` FOREIGN KEY (`conformer`) REFERENCES `Compound` (`uri`),
  CONSTRAINT `FK605882C8F0840213` FOREIGN KEY (`datasetUri`) REFERENCES `Dataset` (`uri`),
  CONSTRAINT `uri_in_dataEntry` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `DataEntryFeatVal`
--

DROP TABLE IF EXISTS `DataEntryFeatVal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DataEntryFeatVal` (
  `dataEntryUri` varchar(255) NOT NULL,
  `featureValUri` varchar(255) NOT NULL,
  `featureVal_IDX` int(11) NOT NULL,
  PRIMARY KEY (`dataEntryUri`,`featureVal_IDX`),
  KEY `featureValueUri_in_DataEntryFeatVal` (`featureValUri`),
  KEY `dataEntryUri_in_DataEntryFeatVal` (`dataEntryUri`),
  CONSTRAINT `dataEntryUri_in_DataEntryFeatVal` FOREIGN KEY (`dataEntryUri`) REFERENCES `DataEntry` (`uri`),
  CONSTRAINT `featureValueUri_in_DataEntryFeatVal` FOREIGN KEY (`featureValUri`) REFERENCES `FeatureValue` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Dataset`
--

DROP TABLE IF EXISTS `Dataset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Dataset` (
  `uri` varchar(255) NOT NULL,
  PRIMARY KEY (`uri`),
  KEY `uri_in_dataset` (`uri`),
  CONSTRAINT `uri_in_dataset` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ErrorReport`
--

DROP TABLE IF EXISTS `ErrorReport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ErrorReport` (
  `uri` varchar(255) NOT NULL,
  `httpStatus` int(11) DEFAULT NULL,
  `actor` varchar(255) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `details` longtext,
  `errorCode` varchar(255) DEFAULT NULL,
  `errorCause` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uri`),
  KEY `FK1BAC153CC61298E4` (`errorCause`),
  KEY `FK1BAC153CB5362BBC` (`uri`),
  CONSTRAINT `FK1BAC153CB5362BBC` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE,
  CONSTRAINT `FK1BAC153CC61298E4` FOREIGN KEY (`errorCause`) REFERENCES `ErrorReport` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FeatAdmVal`
--

DROP TABLE IF EXISTS `FeatAdmVal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FeatAdmVal` (
  `featureUri` varchar(255) NOT NULL,
  `elt` bigint(20) NOT NULL,
  PRIMARY KEY (`featureUri`,`elt`),
  KEY `FK14589E09D53F8953` (`featureUri`),
  KEY `FK14589E0983BF57E7` (`elt`),
  CONSTRAINT `FK14589E0983BF57E7` FOREIGN KEY (`elt`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK14589E09D53F8953` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FeatOntol`
--

DROP TABLE IF EXISTS `FeatOntol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FeatOntol` (
  `featureUri` varchar(255) NOT NULL,
  `DATATYPE_NS` varchar(255) NOT NULL,
  `DATATYPE_Name` varchar(255) NOT NULL,
  `ID_W` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID_W`),
  KEY `featureOntology` (`DATATYPE_NS`,`DATATYPE_Name`),
  KEY `FK5C48ACE0D53F8953` (`featureUri`),
  CONSTRAINT `featureOntology` FOREIGN KEY (`DATATYPE_NS`, `DATATYPE_Name`) REFERENCES `OntologicalClass` (`nameSpace`, `name`),
  CONSTRAINT `FK5C48ACE0D53F8953` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Feature`
--

DROP TABLE IF EXISTS `Feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Feature` (
  `uri` varchar(255) NOT NULL,
  `units` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uri`),
  KEY `uri_in_feature` (`uri`),
  CONSTRAINT `uri_in_feature` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `FeatureValue`
--

DROP TABLE IF EXISTS `FeatureValue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FeatureValue` (
  `uri` varchar(255) NOT NULL,
  `feature` varchar(255) DEFAULT NULL,
  `value` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uri`),
  KEY `FK5156E43BB5362BBC` (`uri`),
  KEY `literal_in_featureValue` (`value`),
  KEY `feature_in_featureValue` (`feature`),
  CONSTRAINT `feature_in_featureValue` FOREIGN KEY (`feature`) REFERENCES `Feature` (`uri`),
  CONSTRAINT `FK5156E43BB5362BBC` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE,
  CONSTRAINT `literal_in_featureValue` FOREIGN KEY (`value`) REFERENCES `Literal` (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Literal`
--

DROP TABLE IF EXISTS `Literal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Literal` (
  `hash` bigint(20) NOT NULL,
  `VALUE` longtext,
  `DATATYPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MetaInfo`
--

DROP TABLE IF EXISTS `MetaInfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MetaInfo` (
  `idx` int(11) NOT NULL AUTO_INCREMENT,
  `_DISCRIM_` int(11) NOT NULL,
  `dateLiteral` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`idx`),
  KEY `FKE8FBC87372A0A07B` (`dateLiteral`),
  CONSTRAINT `FKE8FBC87372A0A07B` FOREIGN KEY (`dateLiteral`) REFERENCES `Literal` (`hash`)
) ENGINE=InnoDB AUTO_INCREMENT=10427 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiAud`
--

DROP TABLE IF EXISTS `MiAud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiAud` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FK46DCF54CCE09048` (`idx`),
  KEY `FK46DCF5489E1352C` (`litId`),
  CONSTRAINT `FK46DCF5489E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK46DCF54CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiComments`
--

DROP TABLE IF EXISTS `MiComments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiComments` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FK2A1ACFD0CCE09048` (`idx`),
  KEY `FK2A1ACFD089E1352C` (`litId`),
  CONSTRAINT `FK2A1ACFD089E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK2A1ACFD0CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiContributors`
--

DROP TABLE IF EXISTS `MiContributors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiContributors` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FK1F425ED4CCE09048` (`idx`),
  KEY `FK1F425ED489E1352C` (`litId`),
  CONSTRAINT `FK1F425ED489E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK1F425ED4CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiCreators`
--

DROP TABLE IF EXISTS `MiCreators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiCreators` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FKBA8477A3CCE09048` (`idx`),
  KEY `FKBA8477A389E1352C` (`litId`),
  CONSTRAINT `FKBA8477A389E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FKBA8477A3CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiDesc`
--

DROP TABLE IF EXISTS `MiDesc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiDesc` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FK894D3E6DCCE09048` (`idx`),
  KEY `FK894D3E6D89E1352C` (`litId`),
  CONSTRAINT `FK894D3E6D89E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK894D3E6DCCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiIDs`
--

DROP TABLE IF EXISTS `MiIDs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiIDs` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FK46DE77CCCE09048` (`idx`),
  KEY `FK46DE77C89E1352C` (`litId`),
  CONSTRAINT `FK46DE77C89E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK46DE77CCCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiPublishers`
--

DROP TABLE IF EXISTS `MiPublishers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiPublishers` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FK4B7C88D3CCE09048` (`idx`),
  KEY `FK4B7C88D389E1352C` (`litId`),
  CONSTRAINT `FK4B7C88D389E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK4B7C88D3CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiSameAs`
--

DROP TABLE IF EXISTS `MiSameAs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiSameAs` (
  `idx` int(11) NOT NULL,
  `rsId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`rsId`),
  KEY `FK84550014CCE09048` (`idx`),
  KEY `FK845500144218B2E7` (`rsId`),
  CONSTRAINT `FK845500144218B2E7` FOREIGN KEY (`rsId`) REFERENCES `Resource` (`hash`),
  CONSTRAINT `FK84550014CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiSeeAlso`
--

DROP TABLE IF EXISTS `MiSeeAlso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiSeeAlso` (
  `idx` int(11) NOT NULL,
  `rsId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`rsId`),
  KEY `FKC9DED5ECCE09048` (`idx`),
  KEY `FKC9DED5E4218B2E7` (`rsId`),
  CONSTRAINT `FKC9DED5E4218B2E7` FOREIGN KEY (`rsId`) REFERENCES `Resource` (`hash`),
  CONSTRAINT `FKC9DED5ECCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiSources`
--

DROP TABLE IF EXISTS `MiSources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiSources` (
  `idx` int(11) NOT NULL,
  `rsId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`rsId`),
  KEY `FK1EA5FE5CCCE09048` (`idx`),
  KEY `FK1EA5FE5C4218B2E7` (`rsId`),
  CONSTRAINT `FK1EA5FE5C4218B2E7` FOREIGN KEY (`rsId`) REFERENCES `Resource` (`hash`),
  CONSTRAINT `FK1EA5FE5CCCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiSubjects`
--

DROP TABLE IF EXISTS `MiSubjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiSubjects` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FKD2A342A3CCE09048` (`idx`),
  KEY `FKD2A342A389E1352C` (`litId`),
  CONSTRAINT `FKD2A342A389E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FKD2A342A3CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MiTitles`
--

DROP TABLE IF EXISTS `MiTitles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MiTitles` (
  `idx` int(11) NOT NULL,
  `litId` bigint(20) NOT NULL,
  PRIMARY KEY (`idx`,`litId`),
  KEY `FK867DE1F7CCE09048` (`idx`),
  KEY `FK867DE1F789E1352C` (`litId`),
  CONSTRAINT `FK867DE1F789E1352C` FOREIGN KEY (`litId`) REFERENCES `Literal` (`hash`),
  CONSTRAINT `FK867DE1F7CCE09048` FOREIGN KEY (`idx`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Model`
--

DROP TABLE IF EXISTS `Model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Model` (
  `uri` varchar(255) NOT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  `dependentFeature` varchar(255) DEFAULT NULL,
  `predictedFeature` varchar(255) DEFAULT NULL,
  `algorithm` varchar(255) DEFAULT NULL,
  `localCode` varchar(255) DEFAULT NULL,
  `dataset` varchar(255) DEFAULT NULL,
  `modData` longblob,
  PRIMARY KEY (`uri`),
  UNIQUE KEY `localCode` (`localCode`),
  KEY `FK4710B096E2C4AC5` (`algorithm`),
  KEY `uri_in_model` (`uri`),
  KEY `FK4710B09BF370DB4` (`dependentFeature`),
  KEY `FK4710B091F9D275B` (`predictedFeature`),
  KEY `FK4710B09A2493D53` (`createdBy`),
  CONSTRAINT `FK4710B091F9D275B` FOREIGN KEY (`predictedFeature`) REFERENCES `Feature` (`uri`),
  CONSTRAINT `FK4710B096E2C4AC5` FOREIGN KEY (`algorithm`) REFERENCES `Algorithm` (`uri`),
  CONSTRAINT `FK4710B09A2493D53` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`),
  CONSTRAINT `FK4710B09BF370DB4` FOREIGN KEY (`dependentFeature`) REFERENCES `Feature` (`uri`),
  CONSTRAINT `uri_in_model` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ModelIndepFeatures`
--

DROP TABLE IF EXISTS `ModelIndepFeatures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ModelIndepFeatures` (
  `modelUri` varchar(255) NOT NULL,
  `featureUri` varchar(255) NOT NULL,
  `indepFeature_idx` int(11) NOT NULL,
  PRIMARY KEY (`modelUri`,`indepFeature_idx`),
  KEY `modelUri_ref_for_independentParameters` (`modelUri`),
  KEY `indepFeatureUri_ref_for_model` (`featureUri`),
  CONSTRAINT `indepFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`),
  CONSTRAINT `modelUri_ref_for_independentParameters` FOREIGN KEY (`modelUri`) REFERENCES `Model` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ModelParams`
--

DROP TABLE IF EXISTS `ModelParams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ModelParams` (
  `modelUri` varchar(255) NOT NULL DEFAULT '',
  `parameterUri` varchar(255) NOT NULL,
  PRIMARY KEY (`modelUri`,`parameterUri`),
  KEY `modelUri_ref_for_Parameters` (`modelUri`),
  KEY `parameterUri_ref_for_model` (`parameterUri`),
  CONSTRAINT `modelUri_ref_for_Parameters` FOREIGN KEY (`modelUri`) REFERENCES `Model` (`uri`),
  CONSTRAINT `parameterUri_ref_for_model` FOREIGN KEY (`parameterUri`) REFERENCES `Parameter` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OTComponent`
--

DROP TABLE IF EXISTS `OTComponent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OTComponent` (
  `uri` varchar(255) NOT NULL,
  `meta` int(11) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uri`),
  UNIQUE KEY `meta` (`meta`),
  KEY `FK3EAD8498CD120F90` (`meta`),
  CONSTRAINT `FK3EAD8498CD120F90` FOREIGN KEY (`meta`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OntologicalClass`
--

DROP TABLE IF EXISTS `OntologicalClass`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OntologicalClass` (
  `nameSpace` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `_DISCRIM_` int(11) NOT NULL,
  `metaInfo` int(11) DEFAULT NULL,
  PRIMARY KEY (`nameSpace`,`name`),
  KEY `FKD6D288A9B20EE9BE` (`metaInfo`),
  CONSTRAINT `FKD6D288A9B20EE9BE` FOREIGN KEY (`metaInfo`) REFERENCES `MetaInfo` (`idx`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OntologicalClass_disjointWith`
--

DROP TABLE IF EXISTS `OntologicalClass_disjointWith`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OntologicalClass_disjointWith` (
  `resource_NS` varchar(255) NOT NULL,
  `resource_name` varchar(255) NOT NULL,
  `disjointWith_NS` varchar(255) NOT NULL,
  `disjointWith_name` varchar(255) NOT NULL,
  PRIMARY KEY (`resource_NS`,`resource_name`,`disjointWith_NS`,`disjointWith_name`),
  KEY `FK736B4F78AB557573` (`resource_NS`,`resource_name`),
  KEY `FK736B4F787227228B` (`disjointWith_NS`,`disjointWith_name`),
  CONSTRAINT `FK736B4F787227228B` FOREIGN KEY (`disjointWith_NS`, `disjointWith_name`) REFERENCES `OntologicalClass` (`nameSpace`, `name`),
  CONSTRAINT `FK736B4F78AB557573` FOREIGN KEY (`resource_NS`, `resource_name`) REFERENCES `OntologicalClass` (`nameSpace`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `OntologicalClass_superClasses`
--

DROP TABLE IF EXISTS `OntologicalClass_superClasses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OntologicalClass_superClasses` (
  `resource_NS` varchar(255) NOT NULL,
  `resource_name` varchar(255) NOT NULL,
  `superclass_NS` varchar(255) NOT NULL,
  `superclass_name` varchar(255) NOT NULL,
  PRIMARY KEY (`resource_NS`,`resource_name`,`superclass_NS`,`superclass_name`),
  KEY `FK78680C01AB557573` (`resource_NS`,`resource_name`),
  KEY `FK78680C01BC10E2D5` (`superclass_NS`,`superclass_name`),
  CONSTRAINT `FK78680C01AB557573` FOREIGN KEY (`resource_NS`, `resource_name`) REFERENCES `OntologicalClass` (`nameSpace`, `name`),
  CONSTRAINT `FK78680C01BC10E2D5` FOREIGN KEY (`superclass_NS`, `superclass_name`) REFERENCES `OntologicalClass` (`nameSpace`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Parameter`
--

DROP TABLE IF EXISTS `Parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Parameter` (
  `uri` varchar(255) NOT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `typedValue` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uri`),
  KEY `parameterUri_references_OTComponent` (`uri`),
  KEY `typedValueInParameter_references_Literal` (`typedValue`),
  CONSTRAINT `parameterUri_references_OTComponent` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE,
  CONSTRAINT `typedValueInParameter_references_Literal` FOREIGN KEY (`typedValue`) REFERENCES `Literal` (`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Resource`
--

DROP TABLE IF EXISTS `Resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Resource` (
  `hash` bigint(20) NOT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `resourceType_NS` varchar(255) DEFAULT NULL,
  `resourceType_Name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`hash`),
  UNIQUE KEY `uri` (`uri`),
  KEY `FKEF86282E716D36FF` (`resourceType_NS`,`resourceType_Name`),
  CONSTRAINT `FKEF86282E716D36FF` FOREIGN KEY (`resourceType_NS`, `resourceType_Name`) REFERENCES `OntologicalClass` (`nameSpace`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Task`
--

DROP TABLE IF EXISTS `Task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Task` (
  `uri` varchar(255) NOT NULL,
  `resultUri` varchar(255) DEFAULT NULL,
  `httpStatus` float DEFAULT NULL,
  `percentageCompleted` float DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `errorReport` varchar(255) DEFAULT NULL,
  `createdBy` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uri`),
  KEY `uri_in_task_references_OTComponent` (`uri`),
  KEY `errReport_in_task` (`errorReport`),
  KEY `FK27A9A5A2493D53` (`createdBy`),
  CONSTRAINT `errReport_in_task` FOREIGN KEY (`errorReport`) REFERENCES `ErrorReport` (`uri`),
  CONSTRAINT `FK27A9A5A2493D53` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`),
  CONSTRAINT `uri_in_task_references_OTComponent` FOREIGN KEY (`uri`) REFERENCES `OTComponent` (`uri`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `uid` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-12-14  3:02:54



/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
