-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 02, 2019 at 07:36 AM
-- Server version: 5.6.42
-- PHP Version: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ontology_comparison`
--
CREATE DATABASE IF NOT EXISTS `ontology_comparison` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `ontology_comparison`;

-- --------------------------------------------------------

--
-- Table structure for table `class`
--

DROP TABLE IF EXISTS `class`;
CREATE TABLE `class` (
  `ID` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `count` bigint(20) NOT NULL,
  `version_id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `class`
--

INSERT INTO `class` (`ID`, `url`, `label`, `comment`, `count`, `version_id`, `parent_id`) VALUES
(1, 'http://om.cs.uga.edu/prokino/2.0#MSAElement', 'MSAElement', '', 0, 1, NULL),
(2, 'http://om.cs.uga.edu/prokino/2.0#AlignedResidue', 'AlignedResidue', '', 0, 1, 1),
(3, 'http://om.cs.uga.edu/prokino/2.0#Deletion', 'Deletion', '', 0, 1, 1),
(4, 'http://om.cs.uga.edu/prokino/2.0#Insertion', 'Insertion', '', 0, 1, 1),
(5, 'http://om.cs.uga.edu/prokino/2.0/#ProKinOEntity', 'ProKinOEntity', '', 0, 1, NULL),
(6, 'http://om.cs.uga.edu/prokino/2.0/#PhysicalEntity', 'PhysicalEntity', '', 0, 1, 5),
(7, 'http://om.cs.uga.edu/prokino/2.0/#ProteinKinaseDomain', 'ProteinKinaseDomain', '', 0, 1, 6),
(8, 'http://om.cs.uga.edu/prokino/2.0/#Atypicalgroup', 'Atypicalgroup', '', 0, 1, 7),
(9, 'http://om.cs.uga.edu/prokino/2.0/#ABC1family', 'ABC1family', '', 0, 1, 8),
(10, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Asubfamily', 'ABC1-Asubfamily', '', 14, 1, 9),
(11, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-B-plant1subfamily', 'ABC1-B-plant1subfamily', '', 6, 1, 9),
(12, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Bsubfamily', 'ABC1-Bsubfamily', '', 17, 1, 9),
(13, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Csubfamily', 'ABC1-Csubfamily', '', 12, 1, 9),
(14, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Dsubfamily', 'ABC1-Dsubfamily', '', 8, 1, 9),
(15, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Esubfamily', 'ABC1-Esubfamily', '', 2, 1, 9),
(16, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Fsubfamily', 'ABC1-Fsubfamily', '', 5, 1, 9),
(17, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Gsubfamily', 'ABC1-Gsubfamily', '', 1, 1, 9),
(18, 'http://om.cs.uga.edu/prokino/2.0/#ABC1-Hsubfamily', 'ABC1-Hsubfamily', '', 1, 1, 9),
(19, 'http://om.cs.uga.edu/prokino/2.0/#PKLgroup', 'PKLgroup', '', 0, 1, 7),
(20, 'http://om.cs.uga.edu/prokino/2.0/#CAKfamily', 'CAKfamily', '', 3, 1, 19),
(21, 'http://om.cs.uga.edu/prokino/2.0/#ACADsubfamily', 'ACADsubfamily', '', 2, 1, 20),
(22, 'http://om.cs.uga.edu/prokino/2.0/#AFKfamily', 'AFKfamily', '', 4, 1, 8),
(23, 'http://om.cs.uga.edu/prokino/2.0/#AGCgroup', 'AGCgroup', '', 0, 1, 7),
(24, 'http://om.cs.uga.edu/prokino/2.0/#AGC-Sarfamily', 'AGC-Sarfamily', '', 0, 1, 23),
(25, 'http://om.cs.uga.edu/prokino/2.0/#AGC-Uniquefamily', 'AGC-Uniquefamily', '', 8, 1, 23),
(26, 'http://om.cs.uga.edu/prokino/2.0/#AGCfamily', 'AGCfamily', '', 0, 1, 23),
(27, 'http://om.cs.uga.edu/prokino/2.0/#AGC-plant1subfamily', 'AGC-plant1subfamily', '', 2, 1, 26),
(28, 'http://om.cs.uga.edu/prokino/2.0/#AGC1family', 'AGC1family', '', 2, 1, 23),
(29, 'http://om.cs.uga.edu/prokino/2.0/#TKgroup', 'TKgroup', '', 0, 1, 7),
(30, 'http://om.cs.uga.edu/prokino/2.0/#ALKfamily', 'ALKfamily', '', 7, 1, 29),
(31, 'http://om.cs.uga.edu/prokino/2.0/#CAMKgroup', 'CAMKgroup', '', 0, 1, 7),
(32, 'http://om.cs.uga.edu/prokino/2.0/#CAMKLfamily', 'CAMKLfamily', '', 0, 1, 31),
(33, 'http://om.cs.uga.edu/prokino/2.0/#AMPKsubfamily', 'AMPKsubfamily', '', 44, 1, 32),
(34, 'http://om.cs.uga.edu/prokino/2.0/#TKLgroup', 'TKLgroup', '', 0, 1, 7),
(35, 'http://om.cs.uga.edu/prokino/2.0/#ARKfamily', 'ARKfamily', '', 10, 1, 34),
(36, 'http://om.cs.uga.edu/prokino/2.0/#ARMKfamily', 'ARMKfamily', '', 2, 1, 34),
(37, 'http://om.cs.uga.edu/prokino/2.0/#STEgroup', 'STEgroup', '', 0, 1, 7),
(38, 'http://om.cs.uga.edu/prokino/2.0/#STE11family', 'STE11family', '', 0, 1, 37),
(39, 'http://om.cs.uga.edu/prokino/2.0/#ASKsubfamily', 'ASKsubfamily', '', 10, 1, 38),
(40, 'http://om.cs.uga.edu/prokino/2.0/#PIKKfamily', 'PIKKfamily', '', 0, 1, 8),
(41, 'http://om.cs.uga.edu/prokino/2.0/#ATMsubfamily', 'ATMsubfamily', '', 11, 1, 40),
(42, 'http://om.cs.uga.edu/prokino/2.0/#ATRsubfamily', 'ATRsubfamily', '', 14, 1, 40),
(43, 'http://om.cs.uga.edu/prokino/2.0/#Ablfamily', 'Ablfamily', '', 8, 1, 29),
(44, 'http://om.cs.uga.edu/prokino/2.0/#Ackfamily', 'Ackfamily', '', 11, 1, 29),
(45, 'http://om.cs.uga.edu/prokino/2.0/#Othergroup', 'Othergroup', '', 0, 1, 7),
(46, 'http://om.cs.uga.edu/prokino/2.0/#AgaK1family', 'AgaK1family', '', 17, 1, 45),
(47, 'http://om.cs.uga.edu/prokino/2.0/#AktRfamily', 'AktRfamily', '', 15, 1, 23),
(48, 'http://om.cs.uga.edu/prokino/2.0/#Aktfamily', 'Aktfamily', '', 102, 1, 23),
(49, 'http://om.cs.uga.edu/prokino/2.0/#Alphafamily', 'Alphafamily', '', 0, 1, 8),
(50, 'http://om.cs.uga.edu/prokino/2.0/#Alpha-Kineto1subfamily', 'Alpha-Kineto1subfamily', '', 4, 1, 49),
(51, 'http://om.cs.uga.edu/prokino/2.0/#Alpha-Unclassifiedsubfamily', 'Alpha-Unclassifiedsubfamily', '', 18, 1, 49),
(52, 'http://om.cs.uga.edu/prokino/2.0/#Alpha-spongesubfamily', 'Alpha-spongesubfamily', '', 8, 1, 49),
(53, 'http://om.cs.uga.edu/prokino/2.0/#Aurfamily', 'Aurfamily', '', 39, 1, 45),
(54, 'http://om.cs.uga.edu/prokino/2.0/#Axlfamily', 'Axlfamily', '', 6, 1, 29),
(55, 'http://om.cs.uga.edu/prokino/2.0/#GRKfamily', 'GRKfamily', '', 0, 1, 23),
(56, 'http://om.cs.uga.edu/prokino/2.0/#BARKsubfamily', 'BARKsubfamily', '', 8, 1, 55),
(57, 'http://om.cs.uga.edu/prokino/2.0/#BAZfamily', 'BAZfamily', '', 4, 1, 8),
(58, 'http://om.cs.uga.edu/prokino/2.0/#PDHKfamily', 'PDHKfamily', '', 0, 1, 8),
(59, 'http://om.cs.uga.edu/prokino/2.0/#BCKDKsubfamily', 'BCKDKsubfamily', '', 6, 1, 58),
(60, 'http://om.cs.uga.edu/prokino/2.0/#BCRfamily', 'BCRfamily', '', 6, 1, 8),
(61, 'http://om.cs.uga.edu/prokino/2.0/#NAKfamily', 'NAKfamily', '', 2, 1, 45),
(62, 'http://om.cs.uga.edu/prokino/2.0/#BIKEsubfamily', 'BIKEsubfamily', '', 13, 1, 61),
(63, 'http://om.cs.uga.edu/prokino/2.0/#BLVRAfamily', 'BLVRAfamily', '', 2, 1, 8),
(64, 'http://om.cs.uga.edu/prokino/2.0/#BRDfamily', 'BRDfamily', '', 15, 1, 8),
(65, 'http://om.cs.uga.edu/prokino/2.0/#BRSKsubfamily', 'BRSKsubfamily', '', 9, 1, 32),
(66, 'http://om.cs.uga.edu/prokino/2.0/#TK-assocgroup', 'TK-assocgroup', '', 0, 1, 7),
(67, 'http://om.cs.uga.edu/prokino/2.0/#SH2family', 'SH2family', '', 1, 1, 66),
(68, 'http://om.cs.uga.edu/prokino/2.0/#BSD-SH2subfamily', 'BSD-SH2subfamily', '', 1, 1, 67),
(69, 'http://om.cs.uga.edu/prokino/2.0/#BUBfamily', 'BUBfamily', '', 17, 1, 45),
(70, 'http://om.cs.uga.edu/prokino/2.0/#BiochemicalEvent', 'BiochemicalEvent', '', 0, 1, 5),
(71, 'http://om.cs.uga.edu/prokino/2.0/#Bud32family', 'Bud32family', '', 15, 1, 45),
(72, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-C8family', 'Ciliate-C8family', '', 0, 1, 45),
(73, 'http://om.cs.uga.edu/prokino/2.0/#C2subfamily', 'C2subfamily', '', 28, 1, 72),
(74, 'http://om.cs.uga.edu/prokino/2.0/#C8subfamily', 'C8subfamily', '', 84, 1, 72),
(75, 'http://om.cs.uga.edu/prokino/2.0/#CAK-Unclassifiedsubfamily', 'CAK-Unclassifiedsubfamily', '', 1, 1, 20),
(76, 'http://om.cs.uga.edu/prokino/2.0/#CAK-fungal1subfamily', 'CAK-fungal1subfamily', '', 1, 1, 20),
(77, 'http://om.cs.uga.edu/prokino/2.0/#CAKgroup', 'CAKgroup', '', 0, 1, 7),
(78, 'http://om.cs.uga.edu/prokino/2.0/#CAMK-Ttfamily', 'CAMK-Ttfamily', '', 5, 1, 31),
(79, 'http://om.cs.uga.edu/prokino/2.0/#CAMK-Tvag1family', 'CAMK-Tvag1family', '', 15, 1, 31),
(80, 'http://om.cs.uga.edu/prokino/2.0/#CAMK-Tvag2family', 'CAMK-Tvag2family', '', 7, 1, 31),
(81, 'http://om.cs.uga.edu/prokino/2.0/#CAMK-Tvag3family', 'CAMK-Tvag3family', '', 3, 1, 31),
(82, 'http://om.cs.uga.edu/prokino/2.0/#CAMK-Uniquefamily', 'CAMK-Uniquefamily', '', 20, 1, 31),
(83, 'http://om.cs.uga.edu/prokino/2.0/#CAMK1family', 'CAMK1family', '', 45, 1, 31),
(84, 'http://om.cs.uga.edu/prokino/2.0/#CAMK1csubfamily', 'CAMK1csubfamily', '', 80, 1, 83),
(85, 'http://om.cs.uga.edu/prokino/2.0/#CAMK2family', 'CAMK2family', '', 13, 1, 31),
(86, 'http://om.cs.uga.edu/prokino/2.0/#CAMKKfamily', 'CAMKKfamily', '', 16, 1, 45),
(87, 'http://om.cs.uga.edu/prokino/2.0/#CAMKK-Metasubfamily', 'CAMKK-Metasubfamily', '', 9, 1, 86),
(88, 'http://om.cs.uga.edu/prokino/2.0/#CAMKK-Unclassifiedsubfamily', 'CAMKK-Unclassifiedsubfamily', '', 9, 1, 86),
(89, 'http://om.cs.uga.edu/prokino/2.0/#CAMKL-Tvag1subfamily', 'CAMKL-Tvag1subfamily', '', 132, 1, 32),
(90, 'http://om.cs.uga.edu/prokino/2.0/#CAMKL-Tvag2subfamily', 'CAMKL-Tvag2subfamily', '', 70, 1, 32),
(91, 'http://om.cs.uga.edu/prokino/2.0/#CAMKL-Tvag3subfamily', 'CAMKL-Tvag3subfamily', '', 5, 1, 32),
(92, 'http://om.cs.uga.edu/prokino/2.0/#CAMKL-Tvag4subfamily', 'CAMKL-Tvag4subfamily', '', 19, 1, 32),
(93, 'http://om.cs.uga.edu/prokino/2.0/#CAMKL-Unclassifiedsubfamily', 'CAMKL-Unclassifiedsubfamily', '', 173, 1, 32),
(94, 'http://om.cs.uga.edu/prokino/2.0/#CASKfamily', 'CASKfamily', '', 5, 1, 31),
(95, 'http://om.cs.uga.edu/prokino/2.0/#CCK4family', 'CCK4family', '', 5, 1, 29),
(96, 'http://om.cs.uga.edu/prokino/2.0/#CMGCgroup', 'CMGCgroup', '', 0, 1, 7),
(97, 'http://om.cs.uga.edu/prokino/2.0/#CDKfamily', 'CDKfamily', '', 1, 1, 96),
(98, 'http://om.cs.uga.edu/prokino/2.0/#CCRKsubfamily', 'CCRKsubfamily', '', 5, 1, 97),
(99, 'http://om.cs.uga.edu/prokino/2.0/#CDC15subfamily', 'CDC15subfamily', '', 18, 1, 38),
(100, 'http://om.cs.uga.edu/prokino/2.0/#CDC2-smoe1subfamily', 'CDC2-smoe1subfamily', '', 9, 1, 97),
(101, 'http://om.cs.uga.edu/prokino/2.0/#CDC2-smoe2subfamily', 'CDC2-smoe2subfamily', '', 13, 1, 97),
(102, 'http://om.cs.uga.edu/prokino/2.0/#CDC2subfamily', 'CDC2subfamily', '', 37, 1, 97),
(103, 'http://om.cs.uga.edu/prokino/2.0/#CDC7family', 'CDC7family', '', 17, 1, 45),
(104, 'http://om.cs.uga.edu/prokino/2.0/#CDK-DD1subfamily', 'CDK-DD1subfamily', '', 4, 1, 97),
(105, 'http://om.cs.uga.edu/prokino/2.0/#CDK-Unclassifiedsubfamily', 'CDK-Unclassifiedsubfamily', '', 18, 1, 97),
(106, 'http://om.cs.uga.edu/prokino/2.0/#CDK10subfamily', 'CDK10subfamily', '', 5, 1, 97),
(107, 'http://om.cs.uga.edu/prokino/2.0/#CDK11subfamily', 'CDK11subfamily', '', 9, 1, 97),
(108, 'http://om.cs.uga.edu/prokino/2.0/#CDK20subfamily', 'CDK20subfamily', '', 2, 1, 97),
(109, 'http://om.cs.uga.edu/prokino/2.0/#CDK2subfamily', 'CDK2subfamily', '', 8, 1, 97),
(110, 'http://om.cs.uga.edu/prokino/2.0/#CDK4subfamily', 'CDK4subfamily', '', 7, 1, 97),
(111, 'http://om.cs.uga.edu/prokino/2.0/#CDK5subfamily', 'CDK5subfamily', '', 15, 1, 97),
(112, 'http://om.cs.uga.edu/prokino/2.0/#CDK7subfamily', 'CDK7subfamily', '', 14, 1, 97),
(113, 'http://om.cs.uga.edu/prokino/2.0/#CDK8-likesubfamily', 'CDK8-likesubfamily', '', 5, 1, 97),
(114, 'http://om.cs.uga.edu/prokino/2.0/#CDK8subfamily', 'CDK8subfamily', '', 12, 1, 97),
(115, 'http://om.cs.uga.edu/prokino/2.0/#CDK9subfamily', 'CDK9subfamily', '', 9, 1, 97),
(116, 'http://om.cs.uga.edu/prokino/2.0/#CDKBsubfamily', 'CDKBsubfamily', '', 4, 1, 97),
(117, 'http://om.cs.uga.edu/prokino/2.0/#CDKLfamily', 'CDKLfamily', '', 24, 1, 96),
(118, 'http://om.cs.uga.edu/prokino/2.0/#CDPKfamily', 'CDPKfamily', '', 24, 1, 31),
(119, 'http://om.cs.uga.edu/prokino/2.0/#CDPK-Unclassifiedsubfamily', 'CDPK-Unclassifiedsubfamily', '', 5, 1, 118),
(120, 'http://om.cs.uga.edu/prokino/2.0/#CDPKasubfamily', 'CDPKasubfamily', '', 4, 1, 118),
(121, 'http://om.cs.uga.edu/prokino/2.0/#CDPKbsubfamily', 'CDPKbsubfamily', '', 4, 1, 118),
(122, 'http://om.cs.uga.edu/prokino/2.0/#CDPKdsubfamily', 'CDPKdsubfamily', '', 3, 1, 118),
(123, 'http://om.cs.uga.edu/prokino/2.0/#CDPKesubfamily', 'CDPKesubfamily', '', 3, 1, 118),
(124, 'http://om.cs.uga.edu/prokino/2.0/#CDPKfsubfamily', 'CDPKfsubfamily', '', 3, 1, 118),
(125, 'http://om.cs.uga.edu/prokino/2.0/#CH-SH2subfamily', 'CH-SH2subfamily', '', 1, 1, 67),
(126, 'http://om.cs.uga.edu/prokino/2.0/#CHK1subfamily', 'CHK1subfamily', '', 14, 1, 32),
(127, 'http://om.cs.uga.edu/prokino/2.0/#CIPK1subfamily', 'CIPK1subfamily', '', 2, 1, 32),
(128, 'http://om.cs.uga.edu/prokino/2.0/#CIPK2subfamily', 'CIPK2subfamily', '', 8, 1, 32),
(129, 'http://om.cs.uga.edu/prokino/2.0/#CIPKsubfamily', 'CIPKsubfamily', '', 6, 1, 32),
(130, 'http://om.cs.uga.edu/prokino/2.0/#CK1group', 'CK1group', '', 0, 1, 7),
(131, 'http://om.cs.uga.edu/prokino/2.0/#CK1family', 'CK1family', '', 0, 1, 130),
(132, 'http://om.cs.uga.edu/prokino/2.0/#CK1-Asubfamily', 'CK1-Asubfamily', '', 9, 1, 131),
(133, 'http://om.cs.uga.edu/prokino/2.0/#CK1-Dsubfamily', 'CK1-Dsubfamily', '', 24, 1, 131),
(134, 'http://om.cs.uga.edu/prokino/2.0/#CK1-Gsubfamily', 'CK1-Gsubfamily', '', 15, 1, 131),
(135, 'http://om.cs.uga.edu/prokino/2.0/#CK1-Unclassifiedsubfamily', 'CK1-Unclassifiedsubfamily', '', 58, 1, 131),
(136, 'http://om.cs.uga.edu/prokino/2.0/#CK1-Uniquefamily', 'CK1-Uniquefamily', '', 6, 1, 130),
(137, 'http://om.cs.uga.edu/prokino/2.0/#CK1xsubfamily', 'CK1xsubfamily', '', 2, 1, 131),
(138, 'http://om.cs.uga.edu/prokino/2.0/#CK1ysubfamily', 'CK1ysubfamily', '', 11, 1, 131),
(139, 'http://om.cs.uga.edu/prokino/2.0/#CK2family', 'CK2family', '', 46, 1, 96),
(140, 'http://om.cs.uga.edu/prokino/2.0/#CLKfamily', 'CLKfamily', '', 34, 1, 96),
(141, 'http://om.cs.uga.edu/prokino/2.0/#CMGC-GL1family', 'CMGC-GL1family', '', 2, 1, 96),
(142, 'http://om.cs.uga.edu/prokino/2.0/#CMGC-Tvag1family', 'CMGC-Tvag1family', '', 3, 1, 96),
(143, 'http://om.cs.uga.edu/prokino/2.0/#CMGC-Uniquefamily', 'CMGC-Uniquefamily', '', 5, 1, 96),
(144, 'http://om.cs.uga.edu/prokino/2.0/#IRAKfamily', 'IRAKfamily', '', 20, 1, 34),
(145, 'http://om.cs.uga.edu/prokino/2.0/#CR4Lsubfamily', 'CR4Lsubfamily', '', 7, 1, 144),
(146, 'http://om.cs.uga.edu/prokino/2.0/#DMPKfamily', 'DMPKfamily', '', 0, 1, 23),
(147, 'http://om.cs.uga.edu/prokino/2.0/#CRIKsubfamily', 'CRIKsubfamily', '', 5, 1, 146),
(148, 'http://om.cs.uga.edu/prokino/2.0/#CRK7-likesubfamily', 'CRK7-likesubfamily', '', 13, 1, 97),
(149, 'http://om.cs.uga.edu/prokino/2.0/#CRK7-plant1subfamily', 'CRK7-plant1subfamily', '', 2, 1, 97),
(150, 'http://om.cs.uga.edu/prokino/2.0/#CRK7subfamily', 'CRK7subfamily', '', 21, 1, 97),
(151, 'http://om.cs.uga.edu/prokino/2.0/#CTKAfamily', 'CTKAfamily', '', 3, 1, 29),
(152, 'http://om.cs.uga.edu/prokino/2.0/#CTKBfamily', 'CTKBfamily', '', 2, 1, 29),
(153, 'http://om.cs.uga.edu/prokino/2.0/#CZAKfamily', 'CZAKfamily', '', 3, 1, 34),
(154, 'http://om.cs.uga.edu/prokino/2.0/#Disease', 'Disease', '', 0, 1, 5),
(155, 'http://om.cs.uga.edu/prokino/2.0/#Cancer', 'Cancer', '', 308, 1, 154),
(156, 'http://om.cs.uga.edu/prokino/2.0/#Cblsubfamily', 'Cblsubfamily', '', 1, 1, 67),
(157, 'http://om.cs.uga.edu/prokino/2.0/#ChaKsubfamily', 'ChaKsubfamily', '', 4, 1, 49),
(158, 'http://om.cs.uga.edu/prokino/2.0/#Chimerasubfamily', 'Chimerasubfamily', '', 1, 1, 67),
(159, 'http://om.cs.uga.edu/prokino/2.0/#Chk1Lfamily', 'Chk1Lfamily', '', 5, 1, 31),
(160, 'http://om.cs.uga.edu/prokino/2.0/#ChoKsubfamily', 'ChoKsubfamily', '', 4, 1, 20),
(161, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A1family', 'Ciliate-A1family', '', 4, 1, 45),
(162, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A2family', 'Ciliate-A2family', '', 14, 1, 45),
(163, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A3family', 'Ciliate-A3family', '', 4, 1, 45),
(164, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A4family', 'Ciliate-A4family', '', 6, 1, 45),
(165, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A5family', 'Ciliate-A5family', '', 11, 1, 45),
(166, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A7family', 'Ciliate-A7family', '', 11, 1, 45),
(167, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A8family', 'Ciliate-A8family', '', 14, 1, 45),
(168, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-A9family', 'Ciliate-A9family', '', 6, 1, 45),
(169, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-B1family', 'Ciliate-B1family', '', 10, 1, 45),
(170, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-B2family', 'Ciliate-B2family', '', 9, 1, 45),
(171, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-B3family', 'Ciliate-B3family', '', 6, 1, 45),
(172, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-C1family', 'Ciliate-C1family', '', 7, 1, 31),
(173, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-C3family', 'Ciliate-C3family', '', 7, 1, 45),
(174, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-C4family', 'Ciliate-C4family', '', 7, 1, 45),
(175, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-C5family', 'Ciliate-C5family', '', 19, 1, 45),
(176, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-C6family', 'Ciliate-C6family', '', 17, 1, 45),
(177, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-C7family', 'Ciliate-C7family', '', 53, 1, 45),
(178, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-D1family', 'Ciliate-D1family', '', 130, 1, 45),
(179, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E1family', 'Ciliate-E1family', '', 58, 1, 45),
(180, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E2family', 'Ciliate-E2family', '', 1, 1, 45),
(181, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E2-Unclassifiedsubfamily', 'Ciliate-E2-Unclassifiedsubfamily', '', 50, 1, 180),
(182, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E2asubfamily', 'Ciliate-E2asubfamily', '', 25, 1, 180),
(183, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E2bsubfamily', 'Ciliate-E2bsubfamily', '', 11, 1, 180),
(184, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E2csubfamily', 'Ciliate-E2csubfamily', '', 7, 1, 180),
(185, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E3family', 'Ciliate-E3family', '', 7, 1, 45),
(186, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E4family', 'Ciliate-E4family', '', 8, 1, 45),
(187, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E5family', 'Ciliate-E5family', '', 12, 1, 45),
(188, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E6family', 'Ciliate-E6family', '', 12, 1, 45),
(189, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E7family', 'Ciliate-E7family', '', 2, 1, 45),
(190, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E8family', 'Ciliate-E8family', '', 4, 1, 45),
(191, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-E9family', 'Ciliate-E9family', '', 3, 1, 45),
(192, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-F1family', 'Ciliate-F1family', '', 2, 1, 45),
(193, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-F2family', 'Ciliate-F2family', '', 3, 1, 45),
(194, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-Gfamily', 'Ciliate-Gfamily', '', 1, 1, 45),
(195, 'http://om.cs.uga.edu/prokino/2.0/#Ciliate-Uniquefamily', 'Ciliate-Uniquefamily', '', 12, 1, 45),
(196, 'http://om.cs.uga.edu/prokino/2.0/#Clade', 'Clade', '', 4, 1, 5),
(197, 'http://om.cs.uga.edu/prokino/2.0/#Mutation', 'Mutation', '', 0, 1, 5),
(198, 'http://om.cs.uga.edu/prokino/2.0/#SubstitutionMutation', 'SubstitutionMutation', '', 0, 1, 197),
(199, 'http://om.cs.uga.edu/prokino/2.0/#CodingSilent', 'CodingSilent', '', 23900, 1, 198),
(200, 'http://om.cs.uga.edu/prokino/2.0/#Col4A3BPfamily', 'Col4A3BPfamily', '', 2, 1, 8),
(201, 'http://om.cs.uga.edu/prokino/2.0/#OtherEntity', 'OtherEntity', '', 0, 1, 6),
(202, 'http://om.cs.uga.edu/prokino/2.0/#Complex', 'Complex', '', 5678, 1, 201),
(203, 'http://om.cs.uga.edu/prokino/2.0/#ComplexMutation', 'ComplexMutation', '', 0, 1, 197),
(204, 'http://om.cs.uga.edu/prokino/2.0/#ComplexDeletionInframe', 'ComplexDeletionInframe', '', 229, 1, 203),
(205, 'http://om.cs.uga.edu/prokino/2.0/#ComplexFrameshift', 'ComplexFrameshift', '', 17, 1, 203),
(206, 'http://om.cs.uga.edu/prokino/2.0/#ComplexInsertionInframe', 'ComplexInsertionInframe', '', 42, 1, 203),
(207, 'http://om.cs.uga.edu/prokino/2.0/#CompoundSubstitution', 'CompoundSubstitution', '', 46, 1, 203),
(208, 'http://om.cs.uga.edu/prokino/2.0/#Crksubfamily', 'Crksubfamily', '', 2, 1, 67),
(209, 'http://om.cs.uga.edu/prokino/2.0/#Cskfamily', 'Cskfamily', '', 12, 1, 29),
(210, 'http://om.cs.uga.edu/prokino/2.0/#DAPKfamily', 'DAPKfamily', '', 0, 1, 31),
(211, 'http://om.cs.uga.edu/prokino/2.0/#DAPKsubfamily', 'DAPKsubfamily', '', 10, 1, 210),
(212, 'http://om.cs.uga.edu/prokino/2.0/#DCAMKLfamily', 'DCAMKLfamily', '', 12, 1, 31),
(213, 'http://om.cs.uga.edu/prokino/2.0/#DDRfamily', 'DDRfamily', '', 16, 1, 29);

-- --------------------------------------------------------

--
-- Table structure for table `dataset`
--

DROP TABLE IF EXISTS `dataset`;
CREATE TABLE `dataset` (
  `ID` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `graph_name` varchar(255) NOT NULL,
  `endpoint_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `endpoint`
--

DROP TABLE IF EXISTS `endpoint`;
CREATE TABLE `endpoint` (
  `ID` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `endpoint`
--

INSERT INTO `endpoint` (`ID`, `name`, `url`) VALUES
(1, 'localhost', 'http://localhost:8890/sparql'),
(2, 'gumbo', 'http://gumbo.cs.uga.edu:8890/sparql');

-- --------------------------------------------------------

--
-- Table structure for table `property`
--

DROP TABLE IF EXISTS `property`;
CREATE TABLE `property` (
  `ID` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `domain_id` int(11) NOT NULL,
  `object_range_id` int(11) NOT NULL,
  `xsd_type_id` int(11) NOT NULL,
  `comment` varchar(255) NOT NULL,
  `version_id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `triple_type`
--

DROP TABLE IF EXISTS `triple_type`;
CREATE TABLE `triple_type` (
  `ID` int(11) NOT NULL,
  `count` bigint(20) NOT NULL,
  `domain_id` int(11) DEFAULT NULL,
  `predicate_id` int(11) DEFAULT NULL,
  `object_range_id` int(11) DEFAULT NULL,
  `xsd_type_id` int(11) DEFAULT NULL,
  `version_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `version`
--

DROP TABLE IF EXISTS `version`;
CREATE TABLE `version` (
  `ID` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `number` varchar(20) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `version`
--

INSERT INTO `version` (`ID`, `name`, `number`, `date`) VALUES
(1, 'Prokino', '1.0.0', '1970-01-01');

-- --------------------------------------------------------

--
-- Table structure for table `xsd_type`
--

DROP TABLE IF EXISTS `xsd_type`;
CREATE TABLE `xsd_type` (
  `ID` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  `type` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `xsd_type`
--

INSERT INTO `xsd_type` (`ID`, `url`, `type`) VALUES
(1, 'http://test.com/', 'test'),
(2, 'http://test.com/', 'test');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `class`
--
ALTER TABLE `class`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `dataset`
--
ALTER TABLE `dataset`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `endPoint_id` (`endpoint_id`);

--
-- Indexes for table `endpoint`
--
ALTER TABLE `endpoint`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `property`
--
ALTER TABLE `property`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `triple_type`
--
ALTER TABLE `triple_type`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `version`
--
ALTER TABLE `version`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `xsd_type`
--
ALTER TABLE `xsd_type`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `class`
--
ALTER TABLE `class`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=214;

--
-- AUTO_INCREMENT for table `dataset`
--
ALTER TABLE `dataset`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `endpoint`
--
ALTER TABLE `endpoint`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `property`
--
ALTER TABLE `property`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `triple_type`
--
ALTER TABLE `triple_type`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `version`
--
ALTER TABLE `version`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `xsd_type`
--
ALTER TABLE `xsd_type`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
