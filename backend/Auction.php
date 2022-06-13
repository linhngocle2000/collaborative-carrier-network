<?php

namespace CCN;

use CCN\Util\TokenHelper;
use Exception;

class Auction
{
	public static function addAuction($data)
	{
		TokenHelper::assertToken();
		
		$db = Database::getConnection();
		$result = $db->query("INSERT INTO `Auction` (`IsActive`, `Iteration`) VALUES (0, 0)");
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
		
		$query = "SELECT `ID`, `Auctioneer`, `IsActive`, `Iteration` FROM `Auction`";

		$db = Database::getConnection();
		$result = $db->query($query);
		if (empty($result))
		{
			throw new Exception('Failed to retrieve auctions');
		}

		$auctions = [];
		while ($row = $result->fetch_assoc())
		{
			$row['IsActive'] = boolval($row['IsActive']);
			$auctions[] = $row;
		}
		return $auctions;
	}

	public static function addTransportRequest($data)
	{
		TokenHelper::assertToken();

		$request = intval($data['TransportRequest']);
		$auction = intval($data['Auction']);

		$db = Database::getConnection();
		$result = $db->query("INSERT INTO `AuctionRequests` (`Auction`, `TransportRequest`) VALUES ($auction, $request)");
		if (empty($result) || $db->affected_rows != 1)
		{
			throw new Exception('Failed to add transport request to auction');
		}
	}
}