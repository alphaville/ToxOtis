-- 
-- DATABASE VERSION : 2.1.0
--
DROP DATABASE IF EXISTS `toxotisdb2`;
CREATE DATABASE `toxotisdb2` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `toxotisdb2`;
--
-- Version
--
DROP TABLE IF EXISTS `Version`;
CREATE TABLE `Version` (
 `First` int(11) NOT NULL,
 `Second` int(11) NOT NULL,
 `Third` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
LOCK TABLE `Version` WRITE;
INSERT INTO `Version` (`First`,`Second`,`Third`) VALUES (2,1,0); 
UNLOCK TABLE ;
--
-- Meta Information (Serialized)
--
DROP TABLE IF EXISTS `MetaInfo`;
CREATE TABLE `MetaInfo` (
 `id` int(11) NOT NULL COMMENT 'HASH Key of MetaInfo obect',
 `meta` blob COMMENT 'Actual Data',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- OpenTox components in General
-- 
DROP TABLE IF EXISTS `OTComponent`;
CREATE TABLE `OTComponent` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL 
      COMMENT 'This is a UUID identifying the component, not a URI',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' 
      COMMENT 'You can virtually "delete" a component without removing it',
  `meta` int(11) COMMENT 'FK to Meta (Hashing)',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP 
      COMMENT 'Creation timestamp',
  `deletionDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' 
      COMMENT 'Disablement timestamp',
  PRIMARY KEY (`id`),
  CONSTRAINT `metainfoFK` FOREIGN KEY (`meta`) 
    REFERENCES `MetaInfo` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  KEY `otcomponent_id_index` (`id`) USING BTREE,
  KEY `index_enabled` (`enabled`) USING BTREE,
  KEY `index_created` (`created`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Users 
--
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `uid` varchar(255) COLLATE utf8_bin NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mail` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `maxParallelTasks` tinyint(4) NOT NULL DEFAULT '10',
  `maxModels` int(11) NOT NULL DEFAULT '2000',
  `maxBibTeX` int(11) NOT NULL DEFAULT '2000',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE KEY `mail` (`mail`) USING BTREE,
  KEY `uid` (`uid`) USING BTREE,
  KEY `index_user_name` (`name`) USING BTREE,
  KEY `index_user_mail` (`mail`) USING BTREE,
  KEY `index_user_maxModels` (`maxModels`) USING BTREE,
  KEY `index_user_maxBibTeX` (`maxBibTeX`) USING BTREE,
  KEY `index_user_maxParallelTasks` (`maxParallelTasks`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Table `User` is initialized with some data
--
LOCK TABLE `User` WRITE;
INSERT INTO `User` (`uid`,`name`,`mail`,`password`,`maxParallelTasks`,`maxModels`,`maxBibTeX`) 
VALUES ('guest@opensso.in-silico.ch','Guest','anonymous@anonymous.org',
             '{SSHA}ficDnnD49QMLnwStKABXzDvFIgrd/c4H',5,2000,2000);
UNLOCK TABLE ;
--
-- BibTeX references
-- 
DROP TABLE IF EXISTS `BibTeX`;
CREATE TABLE `BibTeX` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `abstract` text COLLATE utf8_bin,
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `annotation` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `author` varchar(255) COLLATE utf8_bin NOT NULL,
  `bibType` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bookTitle` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `chapter` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `copyright` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `crossref` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `edition` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `editor` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `isbn` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `issn` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `journal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bibkey` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `keywords` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `number` int(11) unsigned DEFAULT NULL,
  `pages` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `series` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `volume` int(11) unsigned DEFAULT NULL,
  `year` int(11) unsigned DEFAULT NULL,
  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT 'guest@opensso.in-silico.ch',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_bibtex_id` (`id`) USING BTREE,
  KEY `index_bibtex_createdBy` (`createdBy`) USING BTREE,
  KEY `index_bibtex_author` (`author`) USING BTREE,
  KEY `index_bibtex_booktitle` (`bookTitle`) USING BTREE,
  KEY `index_bibtex_url` (`url`) USING BTREE,
  CONSTRAINT `bibtex_extends_component_key` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bibtex_user_reference` FOREIGN KEY (`createdBy`) 
    REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
--
-- Foreign Features (most probably on different servers)
-- Note: OUR Features are found in JFeature
-- 
DROP TABLE IF EXISTS `Feature`;
CREATE TABLE `Feature` (
    `uri` varchar(255) COLLATE utf8_bin NOT NULL,
    `units` varchar(16) COLLATE utf8_bin,
    PRIMARY KEY USING BTREE (`uri`),
    KEY `index_units` USING BTREE  (`units`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- OpenTox models
--
DROP TABLE IF EXISTS `Model`;
CREATE TABLE `Model` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `algorithm` varchar(255) COLLATE utf8_bin DEFAULT NULL 
    COMMENT 'the URI or ID of the algorithm',
  `localCode` varchar(255) COLLATE utf8_bin DEFAULT NULL 
    COMMENT 'in case the actual model is found somewhere on the filesystem',
  `dataset` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `actualModel` LONGBLOB
    COMMENT 'The actual model object stored as a BLOB with compression',
  PRIMARY KEY (`id`),
  UNIQUE KEY `localCode` USING BTREE  (`localCode`),
  KEY `index_model_algorithm` USING BTREE  (`algorithm`),
  KEY `index_model_id` USING BTREE  (`id`),
  KEY `index_model_creator` USING BTREE  (`createdBy`),
  KEY `index_modelAlgorithm` USING BTREE  (`algorithm`),
  KEY `index_model_dataset` USING BTREE  (`dataset`),
  CONSTRAINT `fk_model_references_user` FOREIGN KEY (`createdBy`) 
    REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_of_model_references_otcomponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Dependent features of Models
--
DROP TABLE IF EXISTS `ModelDepFeatures`;
CREATE TABLE `ModelDepFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'Points to the table of features',
  `idx` int(11) unsigned DEFAULT 1 NOT NULL 
    COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`idx`),
  KEY `index_mdf_idx` USING BTREE (`idx`),  
  KEY `modelId_ref_for_dependentParameters` USING BTREE  (`modelId`),
  KEY `depFeatureUri_ref_for_model` USING BTREE  (`featureUri`),
  CONSTRAINT `depFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) 
    REFERENCES `Feature` (`uri`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `modelUri_ref_for_dependentParameters` FOREIGN KEY (`modelId`) 
    REFERENCES `Model` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Independent features of Models
--
DROP TABLE IF EXISTS `ModelIndepFeatures`;
CREATE TABLE `ModelIndepFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'Points to the table of features',
  `idx` int(11) unsigned DEFAULT 1 NOT NULL 
    COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`idx`),
  KEY USING BTREE  (`modelId`,`idx`),
  KEY `modelId_ref_for_independentParameters` USING BTREE  (`modelId`),
  KEY `indepFeatureUri_ref_for_model` USING BTREE  (`featureUri`),
  CONSTRAINT `indepFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) 
    REFERENCES `Feature` (`uri`)  ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `modelUri_ref_for_independentParameters` FOREIGN KEY (`modelId`) 
    REFERENCES `Model` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Predicted features of Models
--
DROP TABLE IF EXISTS `ModelPredictedFeatures`;
CREATE TABLE `ModelPredictedFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'Points to the table of features',
  `idx` int(11) unsigned DEFAULT 1 NOT NULL 
    COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`idx`),
  KEY USING BTREE  (`featureUri`,`idx`),
  KEY `modelId_ref_for_predictedParameters` USING BTREE  (`modelId`),
  KEY `predFeatureUri_ref_for_model` USING BTREE  (`featureUri`),
  CONSTRAINT `predFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) 
    REFERENCES `Feature` (`uri`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `modelUri_ref_for_predictedParameters` FOREIGN KEY (`modelId`) 
    REFERENCES `Model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Model Parameters
--
DROP TABLE IF EXISTS `Parameter`;
CREATE TABLE `Parameter` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `name` varchar(16) COLLATE utf8_bin NOT NULL,
  `scope` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT "OPTIONAL",
  `value` varchar(255) COLLATE utf8_bin DEFAULT "",
  `valueType` varchar(50) COLLATE utf8_bin DEFAULT "string" 
    COMMENT 'can be String, Double, Integer, Float, Double etc',
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'Points to the model table - Many-to-1 relation (every parameter has a model)',
  PRIMARY KEY (`id`),
  KEY `index_parameter_modelId` USING BTREE  (`modelId`),
  KEY `index_parameter_scope` USING BTREE  (`scope`),
  CONSTRAINT `modelUri_ref_for_parameter` FOREIGN KEY (`modelId`) 
    REFERENCES `Model` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `scopeValues` CHECK (UPPER(`scope`) IN (`OPTIONAL`,`MANDATORY`)) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Error Repots
--
DROP TABLE IF EXISTS `ErrorReport`;
CREATE TABLE `ErrorReport` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `httpStatus` int(11) DEFAULT NULL,
  `actor` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `message` text COLLATE utf8_bin DEFAULT NULL,
  `details` longtext,
  `errorCode` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `errorCause` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_errorCause` USING BTREE  (`errorCause`),
  KEY `index_error_id` USING BTREE  (`id`),
  CONSTRAINT `error_id_refs_component` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `trace_fk` FOREIGN KEY (`errorCause`) 
    REFERENCES `ErrorReport` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Asunchronous Tasks
--
DROP TABLE IF EXISTS `Task`;
CREATE TABLE `Task` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `resultUri` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `httpStatus` float DEFAULT 202,
  `percentageCompleted` float DEFAULT 0,
  `status` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `duration` bigint(20) DEFAULT NULL,
  `errorReport` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_uri_in_task_refs_OTComponent` USING BTREE  (`id`),
  KEY `index_errReport_in_task` USING BTREE  (`errorReport`),
  KEY `index_task_creator` USING BTREE  (`createdBy`),
  CONSTRAINT `FK_errReport_in_task` FOREIGN KEY (`errorReport`) 
    REFERENCES `ErrorReport` (`id`),
  CONSTRAINT `FK_task_creator` FOREIGN KEY (`createdBy`) 
    REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_in_task_references_OTComponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Bibliographic references of Models
--
DROP TABLE IF EXISTS `ModelBibTeX`;
CREATE TABLE `ModelBibTeX` (
  `modelId` varchar(50) COLLATE utf8_bin NOT NULL,
  `bibTeXUri` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`modelId`,`bibTeXUri`),
  KEY `index_modelId_in_ModelBibTeX` (`modelId`) USING BTREE,
  KEY `bibTeXId_ref_BibTeX` (`bibTeXUri`),
  CONSTRAINT `modelId_ref_Model` FOREIGN KEY (`modelId`) 
    REFERENCES `Model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Conformers
--
DROP TABLE IF EXISTS `Conformer`;
CREATE TABLE `Conformer` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `id_in_conformer_references_OTComponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Molecular Representation
--
DROP TABLE IF EXISTS `Representation`;
CREATE TABLE `Representation` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `molecularStructure` TEXT COLLATE utf8_bin,
  `representation` varchar(255) COLLATE utf8_bin,
  `reliability` int(11) unsigned DEFAULT 0, 
  PRIMARY KEY (`id`),
  CONSTRAINT `id_represent_references_OTComponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Local features
-- OUR Features: When a client creates a feature on our server, it is stored 
-- in this table. The table Feature holds links to features in foreign/remote
-- servers.
--
DROP TABLE IF EXISTS `JFeature`;
CREATE TABLE `JFeature` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `units` text  COLLATE utf8_bin NOT NULL,
  `type` SET('num','nom','str','dat') NOT NULL,  
  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT 'guest@opensso.in-silico.ch',
  PRIMARY KEY (`id`),
  CONSTRAINT `id_in_jfeature_references_OTComponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `jfeature_user_reference` FOREIGN KEY (`createdBy`) 
    REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--
-- Features Values
--
DROP TABLE IF EXISTS `FeatureValues`;
CREATE TABLE `FeatureValues` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL 
    COMMENT 'ID of the feature value',
  `featureId` varchar(255) COLLATE utf8_bin NOT NULL
    COMMENT 'ID of the (local) feature',
  `valueType` varchar(255) COLLATE utf8_bin NOT NULL
    COMMENT 'String, Int, Double or Blob',
  `stringValue` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `intValue` int(11) COLLATE utf8_bin DEFAULT NULL,
  `doubleValue` int(11) COLLATE utf8_bin DEFAULT NULL,
  `objectValue` LONGBLOB DEFAULT NULL,
  `reliability` int(11) unsigned DEFAULT 0,
  PRIMARY KEY (`id`),
  CONSTRAINT `id_in_featureVal_references_OTComponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `featureId_references_JFeature` FOREIGN KEY (`featureId`)
    REFERENCES `JFeature` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Compound
--
DROP TABLE IF EXISTS `Compound`;
CREATE TABLE `Compound` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL, 
  PRIMARY KEY (`id`),
  CONSTRAINT `id_in_compound_references_OTComponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Compound to Conformers
--
DROP TABLE IF EXISTS `CompoundConformers`;
CREATE TABLE `CompoundConformers` (
  `compoundId` varchar(255) COLLATE utf8_bin NOT NULL, 
  `conformerId` varchar(255) COLLATE utf8_bin NOT NULL, 
  PRIMARY KEY (`compoundId`,`conformerId`),
  CONSTRAINT `compoundId_REF` FOREIGN KEY (`compoundId`) REFERENCES `Compound` (`id`),
  CONSTRAINT `conformerId_REF` FOREIGN KEY (`conformerId`) REFERENCES `Conformer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Datasets
--
DROP TABLE IF EXISTS `Dataset`;
CREATE TABLE `Dataset` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL, 
  PRIMARY KEY (`id`),
  CONSTRAINT `id_in_dataset_references_OTComponent` FOREIGN KEY (`id`) 
    REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Dataset Conformers
--
DROP TABLE IF EXISTS `DatasetConformers`;
CREATE TABLE `DatasetConformers` (
  `datasetId` varchar(255) COLLATE utf8_bin NOT NULL, 
  `conformerId` varchar(255) COLLATE utf8_bin NOT NULL, 
  PRIMARY KEY (`datasetId`,`conformerId`),
  CONSTRAINT `datasetId_REF` FOREIGN KEY (`datasetId`) REFERENCES `Dataset` (`id`),
  CONSTRAINT `conformerId_REF_inDatasetConformers` FOREIGN KEY (`conformerId`) REFERENCES `Conformer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Dataset Features
--
DROP TABLE IF EXISTS `DatasetFeatures`;
CREATE TABLE `DatasetFeatures` (
  `datasetId` varchar(255) COLLATE utf8_bin NOT NULL, 
  `featureId` varchar(255) COLLATE utf8_bin NOT NULL, 
  PRIMARY KEY (`datasetId`,`featureId`),
  CONSTRAINT `datasetId_REF2` FOREIGN KEY (`datasetId`) REFERENCES `Dataset` (`id`),
  CONSTRAINT `featureId_REF2` FOREIGN KEY (`featureId`) REFERENCES `JFeature` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
--
-- Some Triggers...
--
DELIMITER $$
CREATE TRIGGER `bibtex_trigger_create` BEFORE INSERT ON BibTeX
FOR EACH ROW BEGIN
    INSERT IGNORE INTO OTComponent (id) VALUES (NEW.id);
END $$
CREATE TRIGGER `jfeature_trigger_create` BEFORE INSERT ON JFeature
FOR EACH ROW BEGIN
    INSERT IGNORE INTO OTComponent (id) VALUES (NEW.id);
END $$
DELIMITER ;
--
-- When deleting a BibTex also delete OTComponent
-- entry
-- 
DELIMITER $$
CREATE TRIGGER `delete_bibtex` BEFORE DELETE ON BibTeX
FOR EACH ROW BEGIN
    DELETE FROM `OTComponent` WHERE OTComponent.`id`=OLD.id;
END $$
DELIMITER ;

--
-- Initialization of BibTeX...
--
LOCK TABLE `BibTeX` WRITE;
INSERT INTO `BibTeX` 
(`id`,`abstract`,`address`,`annotation`,`author`,`bibType`,
 `bookTitle`,`chapter`,`copyright`, `crossref`,`edition`,`editor`,
 `isbn` ,`issn` ,`journal`,`bibkey` ,`keywords`,`number` ,`pages` ,
 `series`,`title` ,  `url` ,`volume` ,`year` ,`createdBy`) VALUES 
( 'caco2', 
  'The correlations between Caco-2 permeability (logPapp) and molecular properties have been investigated. A training set of 77 structurally diverse organic molecules was used to construct significant QSAR models for Caco-2 cell permeation. Cellular permeation was found to depend primarily upon experimental distribution coefficient (logD) at pH = 7.4, high charged polar surface area (HCPSA), and radius of gyration (rgyr). Among these three descriptors, logD may have the largest impact on diffusion through Caco-2 cell because logD shows obvious linear correlation with logPapp (r=0.703) when logD is smaller than 2.0. High polar surface area will be unfavorable to achieve good Caco-2 permeability because higher polar surface area will introduce stronger H-bonding interactions between Caco-2 cells and drugs. The comparison among HCPSA, PSA (polar surface area), and TPSA (topological polar surface area) implies that high-charged atoms may be more important to the interactions between Caco-2 cell and drugs. Besides logD and HCPSA, rgyr is also closely connected with Caco-2 permeabilities. The molecules with larger rgyr are more difficult to cross Caco-2 monolayers than those with smaller rgyr. The descriptors included in the prediction models permit the interpretation in structural terms of the passive permeability process, evidencing the main role of lipholiphicity, H-bonding, and bulk properties. Besides these three molecular descriptors, the influence of other molecular descriptors was also investigated. From the calculated results, it can be found that introducing descriptors concerned with molecular flexibility can improve the linear correlation. The resulting model with four descriptors bears good statistical significance, n = 77, r = 0.82, q = 0.79, s = 0.45, F = 35.7. The actual predictive abilities of the QSAR model were validated through an external validation test set of 23 diverse compounds. The predictions for the tested compounds are as the same accuracy as the compounds of the training set and significantly better than those predicted by using the model reported. The good predictive ability suggests that the proposed model may be a good tool for fast screening of logPapp for compound libraries or large sets of new chemical entities via combinatorial chemistry synthesis.',
  'College of Chemistry and Molecular Engineering, Peking University, Beijing 100871, China', 
  NULL, 'T. J. Hou, W. Zhang, K. Xia, X. B. Qiao, and X. J. Xu' , 
  'Article',NULL,NULL,'Copyright &copy; 2004 American Chemical Society',
  NULL,NULL,NULL,NULL,NULL,'J. Chem. Inf. Comput. Sci.',NULL,NULL,
  5 , '1585-1600' , 'ADME Evaluation in Drug Discovery' , 
  'Correlation of Caco-2 Permeation with Simple Molecular Properties', 
  'http://pubs.acs.org/doi/abs/10.1021/ci049884m' ,44,2004,'guest@opensso.in-silico.ch'),
( 'FastRbfNn-Sarimveis-Alexandridis-Bafas', 
  'A new algorithm for training radial basis function neural networks is presented in this paper. The algorithm, which is based on the subtractive clustering technique, has a number of advantages compared to the traditional learning algorithms, including faster training times and more accurate predictions. Due to these advantages the method proves suitable for developing models for complex nonlinear systems.',
  'National Technical University of Athens, School of Chemical Engineering, 9 Heroon Polytechniou str., Zografou Campus, Athens 15780, Greece',
   NULL,'Sarimveis H., Alexandridis A., Bafas G.','Article',NULL,NULL,'Copyright &copy; 2003 Elsevier Science B.V. All rights reserved.',
   NULL,NULL,NULL,NULL,NULL,'Neurocomputing',NULL,'Radial basis function networks, Training algorithms, Model selection',NULL,'501-505',NULL,
   'A fast training algorithm for RBF networks based on subtractive clustering',
   'http://dx.doi.org/10.1016/S0925-2312(03)00342-4' ,51,2003,'guest@opensso.in-silico.ch');
UNLOCK TABLE;

-- Some initial features that are hosted on our server
LOCK TABLE `JFeature` WRITE;
INSERT INTO JFeature (id,units,type) VALUES 
('smiles','','str'),
('inchi','','str'),
('inchiKey','','str'),
('synonyms','','str'),
('casrn','','str'),
('regDate','',('str,dat')),
('mw','g/mol','num'),
('iupacName','','str');
UNLOCK TABLE;



LOCK TABLE `OTComponent` WRITE;
INSERT IGNORE INTO OTComponent(id) VALUES ('benzene_conf'),('benzene'),('benzene_rep_1'),('benzene_rep_2'),('benzene_rep_3'),('benzene_rep_4');
UNLOCK TABLE;

LOCK TABLE `Representation` WRITE;
INSERT INTO Representation(id,molecularStructure,representation,reliability) VALUES('benzene_rep_1','
C6H6
APtclcactv09040904043D 0   0.00000     0.00000
 
 12 12  0  0  0  0  0  0  0  0999 V2000
    0.1416   -1.3751   -0.0002 C   0  0  0  0  0  0  0  0  0  0  0  0
   -1.1201   -0.8102   -0.0001 C   0  0  0  0  0  0  0  0  0  0  0  0
    1.2616   -0.5649   -0.0002 C   0  0  0  0  0  0  0  0  0  0  0  0
   -1.2616    0.5649   -0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0
    1.1201    0.8102   -0.0001 C   0  0  0  0  0  0  0  0  0  0  0  0
   -0.1416    1.3751    0.0004 C   0  0  0  0  0  0  0  0  0  0  0  0
    0.2522   -2.4494    0.0043 H   0  0  0  0  0  0  0  0  0  0  0  0
   -2.2473    1.0063   -0.0007 H   0  0  0  0  0  0  0  0  0  0  0  0
    1.9951    1.4431   -0.0001 H   0  0  0  0  0  0  0  0  0  0  0  0
   -1.9951   -1.4431   -0.0005 H   0  0  0  0  0  0  0  0  0  0  0  0
    2.2473   -1.0063    0.0001 H   0  0  0  0  0  0  0  0  0  0  0  0
   -0.2522    2.4494   -0.0002 H   0  0  0  0  0  0  0  0  0  0  0  0
  1  2  2  0  0  0  0
  1  3  1  0  0  0  0
  1  7  1  0  0  0  0
  2  4  1  0  0  0  0
  3  5  2  0  0  0  0
  4  6  2  0  0  0  0
  4  8  1  0  0  0  0
  5  9  1  0  0  0  0
  5  6  1  0  0  0  0
  2 10  1  0  0  0  0
  3 11  1  0  0  0  0
  6 12  1  0  0  0  0
M  END
$$$$','sdf',5),
('benzene_rep_2','<?xml version="1.0" encoding="ISO-8859-1"?><list xmlns="http://www.xml-cml.org/schema">
<molecule id="m1" xmlns="http://www.xml-cml.org/schema">
  <atomArray>
    <atom id="a1" elementType="C" x3="0.1416" y3="-1.3751" z3="-2.0E-4" formalCharge="0" isotopeNumber="12"/>
    <atom id="a2" elementType="C" x3="-1.1201" y3="-0.8102" z3="-1.0E-4" formalCharge="0" isotopeNumber="12"/>
    <atom id="a3" elementType="C" x3="1.2616" y3="-0.5649" z3="-2.0E-4" formalCharge="0" isotopeNumber="12"/>
    <atom id="a4" elementType="C" x3="-1.2616" y3="0.5649" z3="-0.0" formalCharge="0" isotopeNumber="12"/>
    <atom id="a5" elementType="C" x3="1.1201" y3="0.8102" z3="-1.0E-4" formalCharge="0" isotopeNumber="12"/>
    <atom id="a6" elementType="C" x3="-0.1416" y3="1.3751" z3="4.0E-4" formalCharge="0" isotopeNumber="12"/>
    <atom id="a7" elementType="H" x3="0.2522" y3="-2.4494" z3="0.0043" formalCharge="0" isotopeNumber="1"/>
    <atom id="a8" elementType="H" x3="-2.2473" y3="1.0063" z3="-7.0E-4" formalCharge="0" isotopeNumber="1"/>
    <atom id="a9" elementType="H" x3="1.9951" y3="1.4431" z3="-1.0E-4" formalCharge="0" isotopeNumber="1"/>
    <atom id="a10" elementType="H" x3="-1.9951" y3="-1.4431" z3="-5.0E-4" formalCharge="0" isotopeNumber="1"/>
    <atom id="a11" elementType="H" x3="2.2473" y3="-1.0063" z3="1.0E-4" formalCharge="0" isotopeNumber="1"/>
    <atom id="a12" elementType="H" x3="-0.2522" y3="2.4494" z3="-2.0E-4" formalCharge="0" isotopeNumber="1"/>
  </atomArray>
  <bondArray>
    <bond id="b1" atomRefs2="a1 a2" order="D"/>
    <bond id="b2" atomRefs2="a1 a3" order="S"/>
    <bond id="b3" atomRefs2="a1 a7" order="S"/>
    <bond id="b4" atomRefs2="a2 a4" order="S"/>
    <bond id="b5" atomRefs2="a3 a5" order="D"/>
    <bond id="b6" atomRefs2="a4 a6" order="D"/>
    <bond id="b7" atomRefs2="a4 a8" order="S"/>
    <bond id="b8" atomRefs2="a5 a9" order="S"/>
    <bond id="b9" atomRefs2="a5 a6" order="S"/>
    <bond id="b10" atomRefs2="a2 a10" order="S"/>
    <bond id="b11" atomRefs2="a3 a11" order="S"/>
    <bond id="b12" atomRefs2="a6 a12" order="S"/>
  </bondArray>
</molecule>
</list>','cml',5),
('benzene_rep_3','c1ccccc1','smi',5),
('benzene_rep_4','InChI=1S/C6H6/c1-2-4-6-5-3-1/h1-6H','inchi',5);
UNLOCK TABLE;

LOCK TABLE `Compound` WRITE;
INSERT IGNORE INTO Compound(id) VALUES ('benzene');
UNLOCK TABLE;

LOCK TABLE `Conformer` WRITE;
INSERT IGNORE INTO Conformer(id) VALUES ('benzene_conf');
UNLOCK TABLE;

LOCK TABLE `CompoundConformers` WRITE;
INSERT IGNORE INTO CompoundConformers(compoundId,conformerId) VALUES ('benzene',  'benzene_conf');
UNLOCK TABLE;
