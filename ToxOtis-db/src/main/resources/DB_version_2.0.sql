--DB Version : 2.0
DROP DATABASE IF EXISTS toxotisdb;
CREATE DATABASE IF NOT EXISTS toxotisdb DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_bin;
USE toxotisdb;
DROP TABLE IF EXISTS `OTComponent`;
CREATE TABLE `OTComponent` (
  `id` varchar(255) collate utf8_bin NOT NULL COMMENT 'This is a UUID identifying the component, not a URI',
  `enabled` boolean DEFAULT true NOT NULL COMMENT 'You can virtually "delete" a component without removing it from the DB',
  `meta` blob DEFAULT NULL COMMENT 'Meta information about the component',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `deletionDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Disablement timestamp',
  PRIMARY KEY (`id`),
  KEY `otcomponent_id_index` USING BTREE (`id`),
  KEY `index_enabled` USING BTREE (`enabled`),
  KEY `index_created` USING BTREE (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
  `uid` varchar(255) COLLATE utf8_bin NOT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mail` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `maxParallelTasks` tinyint DEFAULT 10 NOT NULL,
  `maxModels` int(11) DEFAULT 2000 NOT NULL,
  `maxBibTeX` int(11) DEFAULT 2000 NOT NULL,  
  PRIMARY KEY USING BTREE (`uid`),
  KEY USING BTREE (`uid`),
  UNIQUE KEY USING BTREE (`mail`),
  KEY `index_user_name` USING BTREE (`name`),
  KEY `index_user_mail` USING BTREE (`mail`),
  KEY `index_user_maxModels`USING BTREE (`maxModels`),
  KEY `index_user_maxBibTeX`USING BTREE (`maxBibTeX`),
  KEY `index_user_maxParallelTasks`USING BTREE (`maxParallelTasks`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
LOCK TABLE `User` WRITE;
INSERT INTO `User` (`uid`,`name`,`mail`,`password`,`maxParallelTasks`,`maxModels`,`maxBibTeX`) VALUES ('guest@opensso.in-silico.ch','Guest','anonymous@anonymous.org','{SSHA}ficDnnD49QMLnwStKABXzDvFIgrd/c4H',5,2000,2000);
UNLOCK TABLE ;
DROP TABLE IF EXISTS `BibTeX`;
CREATE TABLE `BibTeX` (
  `id` varchar(255) NOT NULL,
  `abstract` TEXT COLLATE utf8_bin DEFAULT NULL,
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
  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT "guest@opensso.in-silico.ch",
  PRIMARY KEY USING BTREE (`id`),
  KEY `index_bibtex_id` USING BTREE (`id`),
  KEY `index_bibtex_createdBy` USING BTREE (`createdBy`),
  KEY `index_bibtex_author` USING BTREE (`author`),
  KEY `index_bibtex_booktitle` USING BTREE (`bookTitle`),
  KEY `index_bibtex_url` USING BTREE (`url`),
  CONSTRAINT `bibtex_user_reference` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `bibtex_extends_component_key` FOREIGN KEY (`id`) REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

LOCK TABLE `BibTeX` WRITE;
INSERT INTO `BibTeX` (`id`,`abstract`,`address`,`annotation`,`author`,`bibType`,`bookTitle`,`chapter`,
`copyright`, `crossref`,`edition`,`editor`,`isbn` ,`issn` ,`journal`,`bibkey` ,`keywords`,`number` ,`pages` ,`series`,
`title` ,  `url` ,`volume` ,`year` ,`createdBy`) VALUES 
( 'caco2', 'The correlations between Caco-2 permeability (logPapp) and molecular properties have been investigated. A training set of 77 structurally diverse organic molecules was used to construct significant QSAR models for Caco-2 cell permeation. Cellular permeation was found to depend primarily upon experimental distribution coefficient (logD) at pH = 7.4, high charged polar surface area (HCPSA), and radius of gyration (rgyr). Among these three descriptors, logD may have the largest impact on diffusion through Caco-2 cell because logD shows obvious linear correlation with logPapp (r=0.703) when logD is smaller than 2.0. High polar surface area will be unfavorable to achieve good Caco-2 permeability because higher polar surface area will introduce stronger H-bonding interactions between Caco-2 cells and drugs. The comparison among HCPSA, PSA (polar surface area), and TPSA (topological polar surface area) implies that high-charged atoms may be more important to the interactions between Caco-2 cell and drugs. Besides logD and HCPSA, rgyr is also closely connected with Caco-2 permeabilities. The molecules with larger rgyr are more difficult to cross Caco-2 monolayers than those with smaller rgyr. The descriptors included in the prediction models permit the interpretation in structural terms of the passive permeability process, evidencing the main role of lipholiphicity, H-bonding, and bulk properties. Besides these three molecular descriptors, the influence of other molecular descriptors was also investigated. From the calculated results, it can be found that introducing descriptors concerned with molecular flexibility can improve the linear correlation. The resulting model with four descriptors bears good statistical significance, n = 77, r = 0.82, q = 0.79, s = 0.45, F = 35.7. The actual predictive abilities of the QSAR model were validated through an external validation test set of 23 diverse compounds. The predictions for the tested compounds are as the same accuracy as the compounds of the training set and significantly better than those predicted by using the model reported. The good predictive ability suggests that the proposed model may be a good tool for fast screening of logPapp for compound libraries or large sets of new chemical entities via combinatorial chemistry synthesis.',
  'College of Chemistry and Molecular Engineering, Peking University, Beijing 100871, China', 
   NULL       , 'T. J. Hou, W. Zhang, K. Xia, X. B. Qiao, and X. J. Xu' , 
   'Article',  NULL     , NULL    , 'Copyright © 2004 American Chemical Society' ,
    NULL , NULL  , NULL , NULL , NULL , 'J. Chem. Inf. Comput. Sci.' , NULL   , NULL,
   5 , 1585-1600 , 'ADME Evaluation in Drug Discovery' , 'Correlation of Caco-2 Permeation with Simple Molecular Properties', 
   'http://pubs.acs.org/doi/abs/10.1021/ci049884m' ,     44 , 2004 , 'guest@opensso.in-silico.ch' );
UNLOCK TABLE ;
DROP TABLE IF EXISTS `Feature`;
CREATE TABLE `Feature` (
    `uri` varchar(255) COLLATE utf8_bin NOT NULL,
    `units` varchar(16) COLLATE utf8_bin,
    PRIMARY KEY USING BTREE (`uri`),
    KEY `index_units` USING BTREE  (`units`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `Model`;
CREATE TABLE `Model` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `algorithm` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'the URI or ID of the algorithm',
  `localCode` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'in case the actual model is found somewhere on the filesystem',
  `dataset` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `actualModel` longblob COMMENT 'The actual model object stored as a blob with compression',
  PRIMARY KEY (`id`),
  UNIQUE KEY `localCode` USING BTREE  (`localCode`),
  KEY `index_model_algorithm` USING BTREE  (`algorithm`),
  KEY `index_model_id` USING BTREE  (`id`),
  KEY `index_model_creator` USING BTREE  (`createdBy`),
  KEY `index_modelAlgorithm` USING BTREE  (`algorithm`),
  KEY `index_model_dataset` USING BTREE  (`dataset`),
  CONSTRAINT `fk_model_references_user` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_of_model_references_otcomponent` FOREIGN KEY (`id`) REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `ModelDepFeatures`;
CREATE TABLE `ModelDepFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the table of features',
  `idx` int(11) unsigned DEFAULT 1 NOT NULL COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`idx`),
  KEY `index_mdf_idx` USING BTREE (`idx`),  
  KEY `modelId_ref_for_dependentParameters` USING BTREE  (`modelId`),
  KEY `depFeatureUri_ref_for_model` USING BTREE  (`featureUri`),
  CONSTRAINT `depFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `modelUri_ref_for_dependentParameters` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `ModelIndepFeatures`;
CREATE TABLE `ModelIndepFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the table of features',
  `idx` int(11) unsigned DEFAULT 1 NOT NULL COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`idx`),
  KEY USING BTREE  (`modelId`,`idx`),
  KEY `modelId_ref_for_independentParameters` USING BTREE  (`modelId`),
  KEY `indepFeatureUri_ref_for_model` USING BTREE  (`featureUri`),
  CONSTRAINT `indepFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`)  ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `modelUri_ref_for_independentParameters` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `ModelPredictedFeatures`;
CREATE TABLE `ModelPredictedFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the table of features',
  `idx` int(11) unsigned DEFAULT 1 NOT NULL COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`idx`),
  KEY USING BTREE  (`featureUri`,`idx`),
  KEY `modelId_ref_for_predictedParameters` USING BTREE  (`modelId`),
  KEY `predFeatureUri_ref_for_model` USING BTREE  (`featureUri`),
  CONSTRAINT `predFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `modelUri_ref_for_predictedParameters` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `Parameter`;
CREATE TABLE `Parameter` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `name` varchar(16) COLLATE utf8_bin NOT NULL,
  `scope` varchar(16) COLLATE utf8_bin DEFAULT "OPTIONAL",
  `value` varchar(255) COLLATE utf8_bin DEFAULT "",
  `valueType` varchar(50) COLLATE utf8_bin DEFAULT "string" COMMENT 'can be String, Double, Integer, Float, Double etc',
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table - Many to One relation (every parameter has a model)',
  PRIMARY KEY (`id`),
  KEY `index_parameter_modelId` USING BTREE  (`modelId`),
  KEY `index_parameter_scope` USING BTREE  (`scope`),
  CONSTRAINT `modelUri_ref_for_parameter` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`)  ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `scopeValues` CHECK (UPPER(`scope`) IN (`OPTIONAL`,`MANDATORY`)) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


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
  CONSTRAINT `error_id_refs_component` FOREIGN KEY (`id`) REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `trace_fk` FOREIGN KEY (`errorCause`) REFERENCES `ErrorReport` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  CONSTRAINT `FK_errReport_in_task` FOREIGN KEY (`errorReport`) REFERENCES `ErrorReport` (`id`),
  CONSTRAINT `FK_task_creator` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_in_task_references_OTComponent` FOREIGN KEY (`id`) REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


-- NEW (Introduced on May 3, 2011)
-- Many-to-many relationship between models and BibTeX entries
-- One model can have multiple BibTeX references
-- One BibTeX entry can be assigned to more than one models
DROP TABLE IF EXISTS `ModelBibTeX`;
CREATE TABLE `ModelBibTeX` (
    `modelId` varchar(50) COLLATE utf8_bin NOT NULL,
    `bibTeXId` varchar(255) NOT NULL,
    PRIMARY KEY (`modelId`, `bibTeXId`),
    KEY `index_modelId_in_ModelBibTeX` USING BTREE  (`modelId`),
    CONSTRAINT `modelId_ref_Model` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `bibTeXId_ref_BibTeX` FOREIGN KEY (`bibTeXId`) REFERENCES `BibTeX` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--Under Construction
DROP TABLE IF EXISTS `Bookmark`;
CREATE TABLE `Bookmark` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `folder` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `createdBy` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_uri_in_task_refs_OTComponent` USING BTREE  (`id`),
  KEY `index_bookmark_creator` USING BTREE  (`createdBy`),
  CONSTRAINT `FK_task_creator` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_in_bookmark_references_OTComponent` FOREIGN KEY (`id`) REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `Foaf`;
CREATE TABLE `Foaf` (
    `id` varchar(255) COLLATE utf8_bin NOT NULL,
     PRIMARY KEY (`id`),
     KEY `foaf_id` USING BTREE  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `QprfReportFoaf`;
CREATE TABLE `QprfReportFoaf` (
    `qprf` varchar(255) COLLATE utf8_bin NOT NULL,
    `foaf` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`qprf`,`foaf`),
    CONSTRAINT `FK_qprf` FOREIGN KEY (`qprf`) REFERENCES `QprfReport` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_foaf` FOREIGN KEY (`foaf`) REFERENCES `Foaf` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `QprfReport`;
CREATE TABLE `QprfReport` (
    `id` varchar(255) COLLATE utf8_bin NOT NULL,
    `modelUri` varchar(2000) COLLATE utf8_bin NOT NULL,
    `compoundUri` varchar(2000) COLLATE utf8_bin NOT NULL,
    `doaUri` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
    `keywords`  varchar(2000) COLLATE utf8_bin DEFAULT NULL,
    `report_date` BIGINT,
    `model_date` BIGINT,
    `datasetStructuralAnalogues`  varchar(2000) COLLATE utf8_bin DEFAULT NULL,
    `applicabilityDomainResult` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `predictionResult` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `experimentalResult` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `QMRFreference` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
    `more` BLOB DEFAULT NULL COMMENT 'Additional information about he QPRF Report and user input',
    `createdBy` varchar(255) COLLATE utf8_bin DEFAULT "guest@opensso.in-silico.ch",
    PRIMARY KEY (`id`),
    KEY `index_id_QPRFReport` USING BTREE  (`id`),
    KEY `index_modelUri_QPRFReport` USING BTREE  (`modelUri`),
    KEY `index_compoundUri_QPRFReport` USING BTREE  (`compoundUri`),
    KEY `index_doaUri_QPRFReport` USING BTREE  (`doaUri`),
    KEY `index_keywords_QPRFReport` USING BTREE  (`keywords`),
    CONSTRAINT `qprfReprot_user_reference` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;




DELIMITER $$
CREATE TRIGGER `bibtex_trigger_create` BEFORE INSERT ON BibTeX
FOR EACH ROW BEGIN
    INSERT IGNORE INTO OTComponent (id) VALUES (NEW.id);
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `delete_bibtex` BEFORE DELETE ON BibTeX
FOR EACH ROW BEGIN
    DELETE FROM `OTComponent` WHERE OTComponent.`id`=OLD.id;
END $$
DELIMITER ;


