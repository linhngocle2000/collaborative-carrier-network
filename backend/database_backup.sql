-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jul 11, 2022 at 01:51 PM
-- Server version: 5.5.68-MariaDB
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ckh1694`
--

-- --------------------------------------------------------

--
-- Table structure for table `Agent`
--

CREATE TABLE `Agent` (
  `Username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Password` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `IsAuctioneer` tinyint(1) NOT NULL,
  `MinProfit` double NOT NULL COMMENT 'Minimum profit to bid on a transport request',
  `MaxProfit` double NOT NULL COMMENT 'Maximum profit to auction off',
  `DepotLat` decimal(9,6) NOT NULL,
  `DepotLon` decimal(9,6) NOT NULL,
  `PickupBaserate` float NOT NULL,
  `TravelCostPerKM` float NOT NULL,
  `LoadBaserate` float NOT NULL,
  `InternalTravelCostPerKM` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Agent`
--

INSERT INTO `Agent` (`Username`, `Name`, `Password`, `IsAuctioneer`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES
('agentA', 'Agent A', '$2y$10$cnNa1lropI0jxIh3.LfXH.Q/nOI/jIQt2TtLC8bv53bHZ4Me7HaOK', 0, 30, 10, '2.000000', '7.000000', 200, 100, 83, 35),
('agentB', 'Agent B', '$2y$10$cnNa1lropI0jxIh3.LfXH.Q/nOI/jIQt2TtLC8bv53bHZ4Me7HaOK', 0, 20, 1, '33.000000', '35.000000', 122, 100, 100, 32),
('agentC', 'Agent C', '$2y$10$cnNa1lropI0jxIh3.LfXH.Q/nOI/jIQt2TtLC8bv53bHZ4Me7HaOK', 0, 5, 1, '71.000000', '79.000000', 298, 111, 95, 50),
('agentD', 'Agent D', '$2y$10$cnNa1lropI0jxIh3.LfXH.Q/nOI/jIQt2TtLC8bv53bHZ4Me7HaOK', 0, 100, 20, '12.000000', '73.000000', 500, 120, 77, 40),
('agentE', 'Agent E', '$2y$10$cnNa1lropI0jxIh3.LfXH.Q/nOI/jIQt2TtLC8bv53bHZ4Me7HaOK', 0, 150, 30, '80.000000', '25.000000', 350, 130, 111, 32),
('auctioneer', 'Auctioneer', '$2y$10$o2/3qqRroyt2ZvAegZRlcOtLFOqV5gEASIyA5MkEKE6RjQtPAOLGy', 1, 0, 0, '0.000000', '0.000000', 0, 0, 0, 0),
('test', 'test', '$2y$10$J6ET6iGUZ6EJDf3CzxpbbeceBzamOi0d/ENOCeF7UtgufDajVVRpi', 0, 0, 0, '4.000000', '5.000000', 500, 500, 100, 100);

-- --------------------------------------------------------

--
-- Table structure for table `Auction`
--

CREATE TABLE `Auction` (
  `ID` int(11) NOT NULL,
  `IsActive` tinyint(1) NOT NULL,
  `Iteration` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `AuctionRequests`
--

CREATE TABLE `AuctionRequests` (
  `Auction` int(11) NOT NULL,
  `TransportRequest` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Bid`
--

CREATE TABLE `Bid` (
  `ID` int(11) NOT NULL,
  `Agent` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Auction` int(11) NOT NULL,
  `Price` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Message`
--

CREATE TABLE `Message` (
  `ID` int(11) NOT NULL,
  `Sender` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Receiver` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `Auction` int(11) DEFAULT NULL,
  `Content` varchar(100) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `OldTransportRequest`
--

CREATE TABLE `OldTransportRequest` (
  `ID` int(11) NOT NULL,
  `Owner` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Cost` int(11) NOT NULL,
  `PickupLat` decimal(9,6) NOT NULL,
  `PickupLon` decimal(9,6) NOT NULL,
  `DeliveryLat` decimal(9,6) NOT NULL,
  `DeliveryLon` decimal(9,6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `OldTransportRequest`
--

INSERT INTO `OldTransportRequest` (`ID`, `Owner`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES
(102, 'agentA', 0, '30.000000', '67.000000', '52.000000', '33.000000'),
(103, 'agentE', 0, '23.000000', '35.000000', '46.000000', '48.000000'),
(104, 'agentB', 0, '36.000000', '46.000000', '26.000000', '9.000000'),
(105, 'agentB', 0, '41.000000', '17.000000', '54.000000', '43.000000'),
(106, 'agentD', 0, '32.000000', '44.000000', '31.000000', '38.000000'),
(107, 'agentE', 0, '24.000000', '38.000000', '31.000000', '37.000000'),
(108, 'agentC', 0, '28.000000', '26.000000', '58.000000', '13.000000'),
(109, 'agentD', 0, '48.000000', '44.000000', '1.000000', '19.000000'),
(110, 'agentC', 0, '11.000000', '4.000000', '23.000000', '1.000000'),
(111, 'agentA', 0, '1.000000', '45.000000', '1.000000', '66.000000'),
(112, 'agentE', 0, '10.000000', '31.000000', '44.000000', '20.000000'),
(114, 'agentE', 0, '47.000000', '4.000000', '65.000000', '60.000000'),
(115, 'agentB', 0, '58.000000', '23.000000', '19.000000', '50.000000'),
(116, 'agentE', 0, '36.000000', '12.000000', '24.000000', '35.000000'),
(117, 'agentC', 0, '7.000000', '27.000000', '19.000000', '65.000000'),
(118, 'agentE', 0, '10.000000', '60.000000', '14.000000', '0.000000'),
(119, 'agentB', 0, '39.000000', '58.000000', '66.000000', '17.000000'),
(121, 'agentB', 0, '45.000000', '49.000000', '19.000000', '57.000000'),
(122, 'agentD', 0, '58.000000', '27.000000', '47.000000', '62.000000'),
(123, 'agentC', 0, '43.000000', '0.000000', '15.000000', '56.000000'),
(124, 'agentE', 0, '48.000000', '24.000000', '32.000000', '33.000000'),
(125, 'agentD', 0, '48.000000', '25.000000', '63.000000', '28.000000'),
(126, 'agentA', 0, '57.000000', '12.000000', '17.000000', '25.000000'),
(127, 'agentD', 0, '47.000000', '19.000000', '65.000000', '26.000000'),
(128, 'agentD', 0, '31.000000', '65.000000', '25.000000', '31.000000'),
(129, 'agentE', 0, '13.000000', '43.000000', '52.000000', '36.000000'),
(130, 'agentB', 0, '7.000000', '51.000000', '13.000000', '8.000000'),
(131, 'agentE', 0, '36.000000', '25.000000', '28.000000', '56.000000'),
(132, 'agentA', 0, '67.000000', '22.000000', '34.000000', '32.000000'),
(133, 'agentA', 0, '12.000000', '40.000000', '48.000000', '69.000000'),
(134, 'agentE', 0, '27.000000', '6.000000', '54.000000', '9.000000'),
(135, 'agentD', 0, '61.000000', '58.000000', '17.000000', '0.000000'),
(136, 'agentD', 0, '60.000000', '19.000000', '56.000000', '50.000000'),
(137, 'agentE', 0, '30.000000', '45.000000', '47.000000', '56.000000'),
(138, 'agentE', 0, '44.000000', '37.000000', '32.000000', '11.000000'),
(139, 'agentE', 0, '55.000000', '18.000000', '11.000000', '61.000000'),
(140, 'agentE', 0, '34.000000', '23.000000', '38.000000', '69.000000'),
(141, 'agentE', 0, '45.000000', '65.000000', '7.000000', '64.000000'),
(142, 'agentA', 0, '28.000000', '28.000000', '4.000000', '61.000000'),
(143, 'agentC', 0, '30.000000', '43.000000', '59.000000', '5.000000'),
(144, 'agentE', 0, '70.000000', '24.000000', '13.000000', '68.000000'),
(145, 'agentD', 0, '3.000000', '37.000000', '56.000000', '19.000000'),
(146, 'agentA', 0, '9.000000', '67.000000', '69.000000', '40.000000'),
(147, 'agentE', 0, '10.000000', '55.000000', '44.000000', '35.000000'),
(148, 'agentA', 0, '0.000000', '47.000000', '46.000000', '54.000000'),
(149, 'agentE', 0, '17.000000', '65.000000', '6.000000', '0.000000'),
(150, 'agentC', 0, '7.000000', '51.000000', '34.000000', '53.000000'),
(155, 'agentA', 0, '36.000000', '0.000000', '10.000000', '33.000000'),
(920, 'agentB', 0, '180.000000', '147.000000', '137.000000', '138.000000'),
(921, 'agentE', 0, '51.000000', '71.000000', '26.000000', '55.000000'),
(934, 'agentB', 0, '167.000000', '161.000000', '194.000000', '178.000000'),
(986, 'agentE', 0, '27.000000', '102.000000', '22.000000', '136.000000'),
(1012, 'agentE', 0, '3.000000', '96.000000', '15.000000', '123.000000'),
(1025, 'agentB', 0, '143.000000', '118.000000', '136.000000', '145.000000'),
(1039, 'agentE', 0, '186.000000', '8.000000', '229.000000', '8.000000'),
(1054, 'agentE', 0, '106.000000', '84.000000', '71.000000', '72.000000'),
(1065, 'agentE', 0, '34.000000', '142.000000', '79.000000', '132.000000'),
(1071, 'agentE', 0, '45.000000', '175.000000', '83.000000', '150.000000'),
(1077, 'agentE', 0, '40.000000', '79.000000', '31.000000', '118.000000'),
(1088, 'agentE', 0, '136.000000', '161.000000', '119.000000', '117.000000'),
(1092, 'agentE', 0, '112.000000', '82.000000', '102.000000', '63.000000'),
(1114, 'agentE', 0, '99.000000', '66.000000', '73.000000', '62.000000'),
(1125, 'agentE', 0, '147.000000', '135.000000', '129.000000', '132.000000'),
(1138, 'agentE', 0, '50.000000', '40.000000', '83.000000', '3.000000'),
(1148, 'agentE', 0, '91.000000', '184.000000', '50.000000', '182.000000'),
(1149, 'agentE', 0, '83.000000', '50.000000', '119.000000', '82.000000'),
(1155, 'agentE', 0, '127.000000', '52.000000', '109.000000', '80.000000');

-- --------------------------------------------------------

--
-- Table structure for table `Session`
--

CREATE TABLE `Session` (
  `Token` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `Agent` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Expiration` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `Session`
--

INSERT INTO `Session` (`Token`, `Agent`, `Expiration`) VALUES
('0c913f2b2605f888465b24bc3571ccb4', 'agentE', '2022-07-11 07:04:41'),
('0f7aa582a7452385f77bb284ea2fb3f3', 'agentD', '2022-07-11 04:02:31'),
('5437192aa81b6bb0bc25d036eabe2fdb', 'agentC', '2022-06-28 10:21:20'),
('98f6699292e96f3c42669b54abc9dbaf', 'agentA', '2022-07-11 12:24:57'),
('ba0a34c4600df11054f97beaec006568', 'agentB', '2022-07-11 07:03:01'),
('c34f8a3c21e33066be1fd3c262fd0449', 'test', '2022-07-11 15:50:48'),
('c9cd4773d5fcf92966a2162a628109de', 'auctioneer', '2022-07-11 12:40:38');

-- --------------------------------------------------------

--
-- Table structure for table `TransportRequest`
--

CREATE TABLE `TransportRequest` (
  `ID` int(11) NOT NULL,
  `Owner` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Cost` decimal(11,0) NOT NULL,
  `PickupLat` decimal(9,6) NOT NULL,
  `PickupLon` decimal(9,6) NOT NULL,
  `DeliveryLat` decimal(9,6) NOT NULL,
  `DeliveryLon` decimal(9,6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `TransportRequest`
--

INSERT INTO `TransportRequest` (`ID`, `Owner`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES
(102, 'agentA', '0', '30.000000', '67.000000', '52.000000', '33.000000'),
(103, 'agentE', '0', '23.000000', '35.000000', '46.000000', '48.000000'),
(104, 'agentB', '0', '36.000000', '46.000000', '26.000000', '9.000000'),
(105, 'agentB', '0', '41.000000', '17.000000', '54.000000', '43.000000'),
(106, 'agentD', '0', '32.000000', '44.000000', '31.000000', '38.000000'),
(107, 'agentE', '0', '24.000000', '38.000000', '31.000000', '37.000000'),
(108, 'agentC', '0', '28.000000', '26.000000', '58.000000', '13.000000'),
(109, 'agentD', '0', '48.000000', '44.000000', '1.000000', '19.000000'),
(110, 'agentC', '0', '11.000000', '4.000000', '23.000000', '1.000000'),
(111, 'agentA', '0', '1.000000', '45.000000', '1.000000', '66.000000'),
(112, 'agentE', '0', '10.000000', '31.000000', '44.000000', '20.000000'),
(114, 'agentE', '0', '47.000000', '4.000000', '65.000000', '60.000000'),
(115, 'agentB', '0', '58.000000', '23.000000', '19.000000', '50.000000'),
(116, 'agentE', '0', '36.000000', '12.000000', '24.000000', '35.000000'),
(117, 'agentC', '0', '7.000000', '27.000000', '19.000000', '65.000000'),
(118, 'agentE', '0', '10.000000', '60.000000', '14.000000', '0.000000'),
(119, 'agentE', '4250', '39.000000', '58.000000', '66.000000', '17.000000'),
(121, 'agentB', '0', '45.000000', '49.000000', '19.000000', '57.000000'),
(122, 'agentD', '0', '58.000000', '27.000000', '47.000000', '62.000000'),
(123, 'agentC', '0', '43.000000', '0.000000', '15.000000', '56.000000'),
(124, 'agentE', '0', '48.000000', '24.000000', '32.000000', '33.000000'),
(125, 'agentD', '0', '48.000000', '25.000000', '63.000000', '28.000000'),
(126, 'agentA', '0', '57.000000', '12.000000', '17.000000', '25.000000'),
(127, 'agentD', '0', '47.000000', '19.000000', '65.000000', '26.000000'),
(128, 'agentD', '0', '31.000000', '65.000000', '25.000000', '31.000000'),
(129, 'agentE', '0', '13.000000', '43.000000', '52.000000', '36.000000'),
(130, 'agentB', '0', '7.000000', '51.000000', '13.000000', '8.000000'),
(131, 'agentE', '0', '36.000000', '25.000000', '28.000000', '56.000000'),
(132, 'agentA', '0', '67.000000', '22.000000', '34.000000', '32.000000'),
(133, 'agentA', '0', '12.000000', '40.000000', '48.000000', '69.000000'),
(134, 'agentE', '0', '27.000000', '6.000000', '54.000000', '9.000000'),
(135, 'agentD', '0', '61.000000', '58.000000', '17.000000', '0.000000'),
(136, 'agentD', '0', '60.000000', '19.000000', '56.000000', '50.000000'),
(137, 'agentE', '0', '30.000000', '45.000000', '47.000000', '56.000000'),
(138, 'agentE', '0', '44.000000', '37.000000', '32.000000', '11.000000'),
(139, 'agentE', '0', '55.000000', '18.000000', '11.000000', '61.000000'),
(140, 'agentE', '0', '34.000000', '23.000000', '38.000000', '69.000000'),
(141, 'agentE', '0', '45.000000', '65.000000', '7.000000', '64.000000'),
(142, 'agentA', '0', '28.000000', '28.000000', '4.000000', '61.000000'),
(143, 'agentC', '0', '30.000000', '43.000000', '59.000000', '5.000000'),
(144, 'agentE', '0', '70.000000', '24.000000', '13.000000', '68.000000'),
(145, 'agentD', '0', '3.000000', '37.000000', '56.000000', '19.000000'),
(146, 'agentA', '0', '9.000000', '67.000000', '69.000000', '40.000000'),
(147, 'agentE', '0', '10.000000', '55.000000', '44.000000', '35.000000'),
(148, 'agentA', '0', '0.000000', '47.000000', '46.000000', '54.000000'),
(149, 'agentE', '0', '17.000000', '65.000000', '6.000000', '0.000000'),
(150, 'agentC', '0', '7.000000', '51.000000', '34.000000', '53.000000'),
(155, 'agentA', '0', '36.000000', '0.000000', '10.000000', '33.000000'),
(914, 'agentE', '2824', '135.000000', '164.000000', '139.000000', '195.000000'),
(915, 'agentE', '2899', '140.000000', '65.000000', '121.000000', '91.000000'),
(920, 'agentB', '0', '180.000000', '147.000000', '137.000000', '138.000000'),
(921, 'agentE', '0', '51.000000', '71.000000', '26.000000', '55.000000'),
(922, 'agentE', '2483', '74.000000', '57.000000', '74.000000', '30.000000'),
(926, 'agentE', '3539', '77.000000', '100.000000', '37.000000', '96.000000'),
(932, 'agentE', '8713', '64.000000', '89.000000', '55.000000', '131.000000'),
(934, 'agentB', '0', '167.000000', '161.000000', '194.000000', '178.000000'),
(950, 'agentD', '3140', '171.000000', '27.000000', '211.000000', '50.000000'),
(959, 'agentE', '4029', '68.000000', '181.000000', '79.000000', '226.000000'),
(966, 'agentD', '3058', '133.000000', '14.000000', '157.000000', '52.000000'),
(984, 'agentE', '1941', '113.000000', '118.000000', '133.000000', '115.000000'),
(986, 'agentE', '0', '27.000000', '102.000000', '22.000000', '136.000000'),
(1006, 'agentE', '3191', '151.000000', '98.000000', '184.000000', '84.000000'),
(1012, 'agentE', '0', '3.000000', '96.000000', '15.000000', '123.000000'),
(1017, 'agentD', '2137', '61.000000', '160.000000', '42.000000', '135.000000'),
(1025, 'agentB', '0', '143.000000', '118.000000', '136.000000', '145.000000'),
(1030, 'agentE', '3468', '70.000000', '42.000000', '80.000000', '2.000000'),
(1036, 'agentE', '8713', '61.000000', '86.000000', '57.000000', '117.000000'),
(1039, 'agentE', '0', '186.000000', '8.000000', '229.000000', '8.000000'),
(1043, 'agentE', '1747', '178.000000', '95.000000', '189.000000', '109.000000'),
(1054, 'agentE', '0', '106.000000', '84.000000', '71.000000', '72.000000'),
(1055, 'agentE', '4093', '134.000000', '70.000000', '120.000000', '115.000000'),
(1065, 'agentE', '0', '34.000000', '142.000000', '79.000000', '132.000000'),
(1071, 'agentE', '0', '45.000000', '175.000000', '83.000000', '150.000000'),
(1074, 'agentE', '4107', '104.000000', '74.000000', '93.000000', '28.000000'),
(1077, 'agentE', '0', '40.000000', '79.000000', '31.000000', '118.000000'),
(1080, 'agentD', '493', '162.000000', '39.000000', '163.000000', '35.000000'),
(1081, 'agentD', '2070', '90.000000', '166.000000', '120.000000', '161.000000'),
(1088, 'agentE', '0', '136.000000', '161.000000', '119.000000', '117.000000'),
(1092, 'agentE', '0', '112.000000', '82.000000', '102.000000', '63.000000'),
(1093, 'agentE', '8713', '64.000000', '104.000000', '37.000000', '90.000000'),
(1094, 'agentE', '8713', '38.000000', '146.000000', '38.000000', '104.000000'),
(1106, 'agentE', '4126', '74.000000', '10.000000', '118.000000', '-8.000000'),
(1114, 'agentE', '0', '99.000000', '66.000000', '73.000000', '62.000000'),
(1119, 'agentD', '3317', '73.000000', '140.000000', '94.000000', '96.000000'),
(1122, 'agentE', '3572', '147.000000', '190.000000', '122.000000', '158.000000'),
(1125, 'agentE', '0', '147.000000', '135.000000', '129.000000', '132.000000'),
(1126, 'agentE', '3844', '145.000000', '106.000000', '129.000000', '147.000000'),
(1130, 'agentD', '1936', '95.000000', '175.000000', '90.000000', '147.000000'),
(1136, 'agentE', '3459', '34.000000', '57.000000', '74.000000', '45.000000'),
(1138, 'agentE', '0', '50.000000', '40.000000', '83.000000', '3.000000'),
(1144, 'agentE', '1399', '37.000000', '65.000000', '47.000000', '56.000000'),
(1148, 'agentE', '0', '91.000000', '184.000000', '50.000000', '182.000000'),
(1149, 'agentD', '3277', '83.000000', '50.000000', '119.000000', '82.000000'),
(1155, 'agentE', '0', '127.000000', '52.000000', '109.000000', '80.000000'),
(1158, 'agentE', '3830', '105.000000', '35.000000', '74.000000', '66.000000'),
(1164, 'test', '0', '3.000000', '7.000000', '5.000000', '2.000000'),
(1165, 'test', '0', '6.000000', '1.000000', '2.000000', '7.000000'),
(1166, 'test', '0', '8.000000', '2.000000', '6.000000', '10.000000');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Agent`
--
ALTER TABLE `Agent`
  ADD PRIMARY KEY (`Username`);

--
-- Indexes for table `Auction`
--
ALTER TABLE `Auction`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `AuctionRequests`
--
ALTER TABLE `AuctionRequests`
  ADD PRIMARY KEY (`Auction`,`TransportRequest`),
  ADD KEY `TransportRequest` (`TransportRequest`);

--
-- Indexes for table `Bid`
--
ALTER TABLE `Bid`
  ADD PRIMARY KEY (`ID`) USING BTREE,
  ADD KEY `Agent` (`Agent`),
  ADD KEY `Auction` (`Auction`);

--
-- Indexes for table `Message`
--
ALTER TABLE `Message`
  ADD PRIMARY KEY (`ID`),
  ADD UNIQUE KEY `Receiver` (`Receiver`),
  ADD KEY `Auction` (`Auction`),
  ADD KEY `Sender` (`Sender`);

--
-- Indexes for table `OldTransportRequest`
--
ALTER TABLE `OldTransportRequest`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Owner` (`Owner`);

--
-- Indexes for table `Session`
--
ALTER TABLE `Session`
  ADD PRIMARY KEY (`Token`),
  ADD UNIQUE KEY `Agent` (`Agent`);

--
-- Indexes for table `TransportRequest`
--
ALTER TABLE `TransportRequest`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Owner` (`Owner`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Auction`
--
ALTER TABLE `Auction`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=927;

--
-- AUTO_INCREMENT for table `Bid`
--
ALTER TABLE `Bid`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=760;

--
-- AUTO_INCREMENT for table `Message`
--
ALTER TABLE `Message`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `OldTransportRequest`
--
ALTER TABLE `OldTransportRequest`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1161;

--
-- AUTO_INCREMENT for table `TransportRequest`
--
ALTER TABLE `TransportRequest`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1167;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `AuctionRequests`
--
ALTER TABLE `AuctionRequests`
  ADD CONSTRAINT `AuctionRequests_ibfk_2` FOREIGN KEY (`TransportRequest`) REFERENCES `TransportRequest` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `AuctionRequests_ibfk_1` FOREIGN KEY (`Auction`) REFERENCES `Auction` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `Bid`
--
ALTER TABLE `Bid`
  ADD CONSTRAINT `Bid_ibfk_1` FOREIGN KEY (`Agent`) REFERENCES `Agent` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Bid_ibfk_2` FOREIGN KEY (`Auction`) REFERENCES `Auction` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `Message`
--
ALTER TABLE `Message`
  ADD CONSTRAINT `Message_ibfk_1` FOREIGN KEY (`Sender`) REFERENCES `Agent` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Message_ibfk_2` FOREIGN KEY (`Receiver`) REFERENCES `Agent` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Message_ibfk_3` FOREIGN KEY (`Auction`) REFERENCES `Auction` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `OldTransportRequest`
--
ALTER TABLE `OldTransportRequest`
  ADD CONSTRAINT `OldTransportRequest_ibfk_2` FOREIGN KEY (`Owner`) REFERENCES `Agent` (`Username`) ON DELETE CASCADE;

--
-- Constraints for table `Session`
--
ALTER TABLE `Session`
  ADD CONSTRAINT `Session_ibfk_1` FOREIGN KEY (`Agent`) REFERENCES `Agent` (`Username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `TransportRequest`
--
ALTER TABLE `TransportRequest`
  ADD CONSTRAINT `TransportRequest_ibfk_2` FOREIGN KEY (`Owner`) REFERENCES `Agent` (`Username`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
