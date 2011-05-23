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