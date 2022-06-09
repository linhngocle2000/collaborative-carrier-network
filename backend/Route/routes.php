<?php

use CCN\Agent;
use CCN\Database;
use CCN\Route\Handler;
use CCN\TransportRequest;
use CCN\Util\TokenHelper;

$handler = Handler::get();

// Agent routes
$handler->register('register', [Agent::class, 'register']);
$handler->register('login', [Agent::class, 'login']);
$handler->register('getAgent', [Agent::class, 'getAgent']);
$handler->register('getAgents', [Agent::class, 'getAgents']);
$handler->register('getAuctioneers', [Agent::class, 'getAuctioneers']);
$handler->register('getCarriers', [Agent::class, 'getCarriers']);

// Transport request routes
$handler->register('addRequest', [TransportRequest::class, 'addRequest']);
$handler->register('getRequests', [TransportRequest::class, 'getRequests']);
$handler->register('getRequestsOfAgent', [TransportRequest::class, 'getRequestsOfAgent']);

$handler->register('getAuctions', function ($data)
{
	TokenHelper::assertToken();

	$db = Database::getConnection();
	$result = $db->query("SELECT ac.ID, ac.Name AS `AuctionName`, ag.Username, ag.Name AS `AgentName` FROM `Auction` ac JOIN `Agent` ag ON ac.Auctioneer = ag.Username");
	$auctions = [];
	while ($row = $result->fetch_assoc())
	{
		$auctions[] = [
			'ID' => $row['ID'],
			'Name' => $row['AuctionName'],
			'Auctioneer' => [
				'Username' => $row['Username'],
				'Name' => $row['AgentName'],
			],
		];
	}
	return $auctions;
});