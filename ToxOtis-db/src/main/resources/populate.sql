--
-- Table `User` is initialized with some data
--
LOCK TABLE `User` WRITE;
INSERT INTO `User` (`uid`,`name`,`mail`,`password`,`maxParallelTasks`,`maxModels`,`maxBibTeX`) 
VALUES ('guest@opensso.in-silico.ch','Guest','anonymous@anonymous.org',
             '{SSHA}ficDnnD49QMLnwStKABXzDvFIgrd/c4H',5,2000,2000);
UNLOCK TABLE ;

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


--
-- Benzene is registered in the database
--
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
