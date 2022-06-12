<?php

namespace CCN;

use CCN\Util\TokenHelper;
use Exception;

class Auction
{
	public static function addAuction($data)
	{
		TokenHelper::assertToken();
		$agent = Agent::getAgentFromToken(TokenHelper::getToken());
		if (!$agent->isAuctioneer())
		{
			throw new Exception('You can not add an auction if you are not an auctioneer');
		}

		$db = Database::getConnection();
		$auctioneer = $agent->getUsername();
		$type = $db->escape_string($data['Type']);
		$result = $db->query("INSERT INTO `Auction` (`Auctioneer`, `Type`, `IsActive`, `Iteration`) VALUES ('$auctioneer', '$type', 0, 0)");
		if (empty($result))
		{
			throw new Exception('Could not add auction');
		}
		$id = $db->insert_id;
		return $id;
	}

	public static function getAuctions($data)
	{
		TokenHelper::assertToken();
		
		$query = "SELECT `ID`, `Auctioneer`, `Type`, `IsActive`, `Iteration` FROM `Auction`";
		$agent = Agent::getAgentFromToken(TokenHelper::getToken());
		if ($agent->isAuctioneer())
		{
			$username = $agent->getUsername();
			$query .= " WHERE `Auctioneer` = '$username'";
		}

		$db = Database::getConnection();
		$result = $db->query($query);
		if (empty($result))
		{
			throw new Exception('Failed to retrieve auctions');
		}

		$auctions = [];
		while ($row = $result->fetch_assoc())
		{
			$auctions[] = $row;
		}
		return $auctions;
	}

	public static function addTransportRequest($data)
	{
		TokenHelper::assertToken();
		$agent = Agent::getAgentFromToken(TokenHelper::getToken());
		if (!$agent->isAuctioneer())
		{
			throw new Exception('You can not modify an auction if you are not an auctioneer');
		}

		$request = intval($data['TransportRequest']);
		$auction = intval($data['Auction']);

		$db = Database::getConnection();
		$result = $db->query("UPDATE `TransportRequest` SET `Auction` = $auction WHERE `ID` = $request AND `Auction` IS NULL");
		if (empty($result) || $db->affected_rows != 1)
		{
			throw new Exception('Failed to add transport request to auction');
		}
	}
}