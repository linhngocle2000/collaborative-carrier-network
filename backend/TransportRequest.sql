-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jun 20, 2022 at 09:24 AM
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
-- Table structure for table `OldTransportRequest`
--

CREATE TABLE `OldTransportRequest` (
  `ID` int(11) NOT NULL,
  `Owner` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `Cost` int(11) NOT NULL,
  `PickupLat` decimal(8,6) NOT NULL,
  `PickupLon` decimal(9,6) NOT NULL,
  `DeliveryLat` decimal(8,6) NOT NULL,
  `DeliveryLon` decimal(9,6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `OldTransportRequest`
--
ALTER TABLE `OldTransportRequest`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `Owner` (`Owner`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `OldTransportRequest`
--
ALTER TABLE `OldTransportRequest`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `OldTransportRequest`
--
ALTER TABLE `OldTransportRequest`
  ADD CONSTRAINT `OldTransportRequest_ibfk_2` FOREIGN KEY (`Owner`) REFERENCES `Agent` (`Username`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
