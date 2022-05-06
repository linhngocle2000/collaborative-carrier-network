-- phpMyAdmin SQL Dump
-- version 5.1.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 06, 2022 at 10:46 AM
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
-- Database: `cxz1760`
--

-- --------------------------------------------------------

--
-- Table structure for table `Agent`
--

CREATE TABLE `Agent` (
  `ID` int(11) NOT NULL,
  `Name` varchar(30) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Auction`
--

CREATE TABLE `Auction` (
  `ID` int(11) NOT NULL,
  `Auctioneer` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `Bid`
--

CREATE TABLE `Bid` (
  `Agent` int(11) NOT NULL,
  `Auction` int(11) NOT NULL,
  `Request` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `TransportRequest`
--

CREATE TABLE `TransportRequest` (
  `ID` int(11) NOT NULL,
  `Owner` int(11) NOT NULL,
  `Auction` int(11) DEFAULT NULL,
  `Cost` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Agent`
--
ALTER TABLE `Agent`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `Auction`
--
ALTER TABLE `Auction`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Auctioneer` (`Auctioneer`);

--
-- Indexes for table `Bid`
--
ALTER TABLE `Bid`
  ADD UNIQUE KEY `Agent` (`Agent`,`Auction`,`Request`) USING BTREE,
  ADD KEY `Auction` (`Auction`),
  ADD KEY `Request` (`Request`);

--
-- Indexes for table `TransportRequest`
--
ALTER TABLE `TransportRequest`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Owner` (`Owner`),
  ADD KEY `Auction` (`Auction`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Agent`
--
ALTER TABLE `Agent`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `Auction`
--
ALTER TABLE `Auction`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `TransportRequest`
--
ALTER TABLE `TransportRequest`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Auction`
--
ALTER TABLE `Auction`
  ADD CONSTRAINT `Auction_ibfk_1` FOREIGN KEY (`Auctioneer`) REFERENCES `Agent` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `Bid`
--
ALTER TABLE `Bid`
  ADD CONSTRAINT `Bid_ibfk_3` FOREIGN KEY (`Request`) REFERENCES `TransportRequest` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Bid_ibfk_1` FOREIGN KEY (`Agent`) REFERENCES `Agent` (`ID`) ON DELETE CASCADE,
  ADD CONSTRAINT `Bid_ibfk_2` FOREIGN KEY (`Auction`) REFERENCES `Auction` (`ID`) ON DELETE CASCADE;

--
-- Constraints for table `TransportRequest`
--
ALTER TABLE `TransportRequest`
  ADD CONSTRAINT `TransportRequest_ibfk_2` FOREIGN KEY (`Auction`) REFERENCES `Auction` (`ID`) ON DELETE SET NULL,
  ADD CONSTRAINT `TransportRequest_ibfk_1` FOREIGN KEY (`Owner`) REFERENCES `Agent` (`ID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
