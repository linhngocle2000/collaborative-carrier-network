<?php

use CCN\Agent;
use CCN\Database;
use CCN\Route\Handler;
use CCN\Util\TokenHelper;

$handler = Handler::get();

$handler->register('register', function ($data)
{
	Agent::register($data['Username'], $data['Name'], $data['Password']);
	return null;
});

$handler->register('login', function ($data)
{
	return Agent::login($data['Username'], $data['Password']);
});

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