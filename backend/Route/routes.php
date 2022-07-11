<?php

use CCN\Agent;
use CCN\Auction;
use CCN\Database;
use CCN\TransportRequest;
use CCN\Route\Handler;

$handler = Handler::get();

// Agent routes
$handler->register('register', [Agent::class, 'register']);
$handler->register('login', [Agent::class, 'login']);
$handler->register('getAgent', [Agent::class, 'getAgent']);
$handler->register('getAgents', [Agent::class, 'getAgents']);
$handler->register('getAuctioneers', [Agent::class, 'getAuctioneers']);
$handler->register('getCarriers', [Agent::class, 'getCarriers']);
$handler->register('setMinProfit', [Agent::class, 'setMinProfit']);
$handler->register('setMaxProfit', [Agent::class, 'setMaxProfit']);

// Transport request routes
$handler->register('addRequest', [TransportRequest::class, 'addRequest']);
$handler->register('getRequests', [TransportRequest::class, 'getRequests']);
$handler->register('getRequestsOfAgent', [TransportRequest::class, 'getRequestsOfAgent']);
$handler->register('getRequestsOfAuction', [TransportRequest::class, 'getRequestsOfAuction']);
$handler->register('stashRequests', [TransportRequest::class, 'stashRequests']);
$handler->register('getStashedRequests', [TransportRequest::class, 'getStashedRequests']);
$handler->register('resetCost', [TransportRequest::class, 'resetCost']);

// Auction routes
$handler->register('addAuction', [Auction::class, 'addAuction']);
$handler->register('getAuctions', [Auction::class, 'getAuctions']);
$handler->register('addRequestToAuction', [Auction::class, 'addTransportRequest']);
$handler->register('addBid', [Auction::class, 'addBid']);
$handler->register('getBids', [Auction::class, 'getBids']);
$handler->register('startAuction', [Auction::class, 'startAuction']);
$handler->register('endAuction', [Auction::class, 'endAuction']);
$handler->register('setWinner', [Auction::class, 'setWinner']);
$handler->register('resetAuction', [Auction::class, 'resetAuction']);

// Setup test database
$handler->register('init', function ($data)
{
	$agentPW = password_hash('agent', PASSWORD_DEFAULT);
	$auctioneerPW = password_hash('auctioneer', PASSWORD_DEFAULT);

	$queries = [
		"DELETE FROM `Agent` WHERE `Username` != 'postman'",
		"DELETE FROM `Auction`",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `IsAuctioneer`) VALUES ('auctioneer', 'Auctioneer', '$auctioneerPW', 1)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentA', 'Agent A', '$agentPW', 45, 10,  36, 170, 35, 8, 18, 4)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentB', 'Agent B', '$agentPW', 39,  8,  52,  91, 40, 9, 16, 6)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentC', 'Agent C', '$agentPW', 60, 12, 153, 173, 37, 8, 20, 3)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentD', 'Agent D', '$agentPW', 35, 10,   4,  35, 35, 8, 25, 5)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentE', 'Agent E', '$agentPW', 36, 10, 106,  85, 36, 9, 18, 6)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentF', 'Agent F', '$agentPW', 31,  8, 180, 187, 38, 8, 19, 5)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentG', 'Agent G', '$agentPW', 40,  9,  97,  58, 37, 9, 20, 4)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentH', 'Agent H', '$agentPW', 38, 10, 147, 141, 38, 8, 25, 3)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentI', 'Agent I', '$agentPW', 36,  8,  36,  19, 39, 8, 17, 3)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentJ', 'Agent J', '$agentPW', 37,  9, 129, 115, 31, 9, 16, 5)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentK', 'Agent K', '$agentPW', 32,  9,   8, 139, 40, 8, 21, 6)",
		"INSERT INTO `Agent`(`Username`, `Name`, `Password`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('agentL', 'Agent L', '$agentPW', 33, 10,  73, 154, 36, 8, 18, 4)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 77, 4, 105, 76)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 4, 7, 185, 176)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 119, 110, 172, 182)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 52, 46, 42, 34)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 161, 77, 1, 111)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 74, 183, 110, 7)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 80, 119, 147, 2)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 2, 28, 159, 190)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 116, 111, 146, 195)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentA', 46, 63, 42, 28)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 197, 31, 51, 72)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 74, 94, 145, 97)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 73, 180, 143, 55)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 197, 157, 94, 114)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 172, 9, 15, 143)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 121, 123, 91, 139)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 60, 123, 29, 89)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 106, 11, 126, 33)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 45, 49, 171, 20)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentB', 109, 21, 163, 15)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 14, 21, 186, 100)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 73, 165, 85, 184)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 50, 133, 198, 154)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 79, 175, 171, 37)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 96, 10, 137, 181)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 0, 200, 171, 106)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 107, 122, 23, 107)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 20, 165, 120, 39)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 44, 7, 43, 99)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentC', 60, 143, 6, 124)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 3, 77, 190, 115)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 33, 24, 8, 39)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 62, 32, 35, 37)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 31, 100, 58, 145)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 20, 27, 97, 188)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 164, 176, 184, 143)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 196, 43, 30, 140)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 22, 2, 43, 185)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 195, 39, 159, 72)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentD', 148, 86, 44, 54)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 104, 191, 200, 158)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 37, 46, 33, 131)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 77, 5, 164, 79)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 170, 52, 72, 173)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 196, 113, 30, 21)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 150, 70, 12, 182)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 89, 157, 139, 194)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 137, 57, 50, 23)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 29, 99, 109, 141)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentE', 141, 54, 38, 187)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 160, 114, 121, 42)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 174, 40, 166, 48)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 170, 196, 6, 44)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 38, 0, 12, 69)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 99, 63, 145, 63)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 159, 9, 6, 108)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 132, 59, 94, 121)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 43, 33, 168, 104)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 82, 28, 194, 135)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentF', 21, 102, 37, 23)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 120, 9, 88, 61)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 102, 135, 65, 25)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 64, 94, 123, 32)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 8, 167, 45, 194)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 135, 111, 168, 47)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 66, 155, 49, 174)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 119, 138, 163, 166)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 193, 78, 199, 25)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 109, 57, 59, 145)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentG', 98, 69, 74, 177)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 79, 54, 165, 35)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 10, 48, 128, 3)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 163, 152, 134, 149)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 195, 105, 109, 14)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 45, 187, 33, 97)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 35, 124, 192, 173)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 188, 128, 158, 200)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 90, 118, 3, 97)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 2, 161, 188, 69)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentH', 183, 11, 150, 134)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 71, 164, 163, 150)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 199, 28, 58, 97)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 11, 125, 3, 10)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 181, 152, 160, 33)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 14, 16, 173, 102)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 181, 46, 173, 152)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 184, 10, 12, 167)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 149, 126, 193, 116)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 119, 156, 199, 15)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentI', 164, 118, 49, 11)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 0, 129, 116, 75)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 112, 80, 29, 142)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 169, 97, 173, 29)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 170, 31, 119, 173)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 38, 133, 135, 94)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 81, 149, 52, 182)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 67, 119, 156, 71)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 82, 45, 19, 44)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 99, 43, 110, 24)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentJ', 110, 95, 147, 78)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 6, 58, 92, 102)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 102, 67, 45, 167)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 97, 28, 65, 89)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 135, 123, 185, 169)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 42, 3, 136, 32)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 101, 159, 26, 29)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 68, 115, 138, 128)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 125, 160, 127, 150)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 83, 143, 65, 106)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentK', 177, 115, 22, 52)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 151, 35, 25, 128)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 14, 172, 10, 77)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 19, 190, 55, 110)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 79, 125, 179, 155)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 35, 97, 155, 62)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 11, 4, 28, 62)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 124, 169, 130, 152)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 150, 106, 139, 149)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 73, 22, 42, 29)",
		"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agentL', 115, 6, 10, 14)",
	];

	$db = Database::getConnection();
	$db->begin_transaction();
	foreach ($queries as $query)
	{
		$db->query($query);
	}
	$db->commit();
});