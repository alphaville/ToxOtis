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

--SQL for DB schema creation
DROP DATABASE IF EXISTS toxotisdb;
CREATE DATABASE IF NOT EXISTS toxotisdb DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_bin;
USE toxotisdb;
--
--OTComponent
--
DROP TABLE IF EXISTS `OTComponent`;
CREATE TABLE `OTComponent` (
  `id` varchar(255) collate utf8_bin NOT NULL COMMENT 'This is a UUID identifying the component, not a URI',
  `enabled` boolean DEFAULT true NOT NULL COMMENT 'You can virtually "delete" a component without removing it from the DB',
  `meta` blob DEFAULT NULL COMMENT 'Meta information about the component',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `deletionDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'Disablement timestamp',
  PRIMARY KEY (`id`),
  KEY `otcomponent_id_index` USING BTREE (`id`),
  KEY `index_enabled` USING BTREE (`enabled`) on lookup (`enabled`),
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
  `abstract` varchar(255) COLLATE utf8_bin DEFAULT NULL,
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


DROP TABLE IF EXISTS `Feature`;
CREATE TABLE `Feature` (
    `uri` varchar(255) COLLATE utf8_bin NOT NULL,
    `units` varchar(16),
    PRIMARY KEY USING BTREE (`uri`),
    KEY `index_units` (`units`)
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
  UNIQUE KEY `localCode` (`localCode`),
  KEY `index_model_algorithm` (`algorithm`),
  KEY `index_model_id` (`id`),
  KEY `index_model_creator` (`createdBy`),
  KEY `index_modelAlgorithm` (`algorithm`),
  KEY `index_model_dataset` (`dataset`),
  CONSTRAINT `fk_model_references_user` FOREIGN KEY (`createdBy`) REFERENCES `User` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `id_of_model_references_otcomponent` FOREIGN KEY (`id`) REFERENCES `OTComponent` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `ModelDepFeatures`;
CREATE TABLE `ModelDepFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the table of features',
  `depFeature_idx` int(11) unsigned DEFAULT 1 NOT NULL COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`depFeature_idx`),
  KEY (`modelId`,`depFeature_idx`),
  KEY `modelId_ref_for_dependentParameters` (`modelId`),
  KEY `depFeatureUri_ref_for_model` (`featureUri`),
  CONSTRAINT `depFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`),
  CONSTRAINT `modelUri_ref_for_dependentParameters` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `ModelIndepFeatures`;
CREATE TABLE `ModelIndepFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the table of features',
  `indepFeature_idx` int(11) unsigned DEFAULT 1 NOT NULL COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`indepFeature_idx`),
  KEY (`modelId`,`indepFeature_idx`),
  KEY `modelId_ref_for_independentParameters` (`modelId`),
  KEY `indepFeatureUri_ref_for_model` (`featureUri`),
  CONSTRAINT `indepFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`),
  CONSTRAINT `modelUri_ref_for_independentParameters` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DROP TABLE IF EXISTS `ModelPredictedFeatures`;
CREATE TABLE `ModelPredictedFeatures` (
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table',
  `featureUri` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the table of features',
  `predFeature_idx` int(11) unsigned DEFAULT 1 NOT NULL COMMENT 'index of features',
  PRIMARY KEY (`modelId`,`predFeature_idx`),
  KEY (`modelId`,`predFeature_idx`),
  KEY `modelId_ref_for_predictedParameters` (`modelId`),
  KEY `predFeatureUri_ref_for_model` (`featureUri`),
  CONSTRAINT `predFeatureUri_ref_for_model` FOREIGN KEY (`featureUri`) REFERENCES `Feature` (`uri`),
  CONSTRAINT `modelUri_ref_for_predictedParameters` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `Parameter`;
CREATE TABLE `Parameter` (
  `id` varchar(50) COLLATE utf8_bin NOT NULL,
  `name` varchar(16) COLLATE utf8_bin NOT NULL,
  `scope` varchar(16) COLLATE utf8_bin DEFAULT "OPTIONAL",
  `value` varchar(16) COLLATE utf8_bin DEFAULT "",
  `valueType` varchar(16) COLLATE utf8_bin DEFAULT "string" COMMENT 'can be String, Double, Integer, Float, Double etc',
  `modelId` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Points to the model table - Many to One relation (every parameter has a model)',
  PRIMARY KEY (`id`),
  KEY `index_parameter_modelId` (`modelId`),
  KEY `index_parameter_scope` (`scope`),
  CONSTRAINT `modelUri_ref_for_parameter` FOREIGN KEY (`modelId`) REFERENCES `Model` (`id`),
  CONSTRAINT `scopeValues` CHECK (UPPER(`scope`) IN (`OPTIONAL`,`MANDATORY`)) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


DELIMITER $$
CREATE TRIGGER `bibtex_trigger_create` BEFORE INSERT ON BibTeX
FOR EACH ROW BEGIN
    INSERT IGNORE INTO OTComponent (id) VALUES (NEW.id);
END $$
DELIMITER ;



