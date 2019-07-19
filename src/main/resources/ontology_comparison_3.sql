-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 19, 2019 at 10:33 AM
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
  `comment` varchar(255) NOT NULL,
  `count` bigint(20) NOT NULL,
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
  `domain_id` int(11) NOT NULL,
  `predicate_id` int(11) NOT NULL,
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
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

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
