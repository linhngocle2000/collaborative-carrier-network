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
		
		$query = "SELECT `ID`, `IsActive`, `Iteration` FROM `Auction`";

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
		return $db->insert_id;
	}

	public static function addBid($data)
	{
		TokenHelper::assertToken();

		$agent = Agent::getAgentFromToken(TokenHelper::getToken())->getUsername();
		$auction = intval($data['Auction']);
		$price = intval($data['Price']);

		$db = Database::getConnection();
		$result = $db->query("INSERT INTO `Bid` (`Agent`, `Auction`, `Price`) VALUES ('$agent', $auction, $price)");
		if (empty($result) || $db->affected_rows != 1)
		{
			throw new Exception('Failed to add bid');
		}
		return $db->insert_id;
	}

	public static function getBids($data)
	{
		TokenHelper::assertToken();

		$auction = intval($data['Auction']);

		$db = Database::getConnection();
		$result = $db->query("SELECT `ID`, `Agent`, `Auction`, `Price` FROM `Bid` WHERE `Auction` = $auction");
		if (empty($result))
		{
			throw new Exception('Failed to retrieve bids');
		}
		$arr = [];
		while ($row = $result->fetch_assoc())
		{
			$arr[] = $row;
		}
		return $arr;
	}

	public static function startAuction($data)
	{
		TokenHelper::assertToken();
		$agent = Agent::getAgentFromToken(TokenHelper::getToken());
		if (!$agent->isAuctioneer())
		{
			throw new Exception('You can not administrate auctions');
		}

		$db = Database::getConnection();
		$auction = intval($data['Auction']);
		$result = $db->query("UPDATE `Auction` SET `IsActive` = 1, `Iteration` = `Iteration` + 1 WHERE `ID` = $auction");
		if (empty($result))
		{
			throw new Exception('Failed to start auction');
		}
	}

	public static function endAuction($data)
	{
		TokenHelper::assertToken();
		$agent = Agent::getAgentFromToken(TokenHelper::getToken());
		if (!$agent->isAuctioneer())
		{
			throw new Exception('You can not administrate auctions');
		}

		$db = Database::getConnection();
		$auction = intval($data['Auction']);
		$result = $db->query("UPDATE `Auction` SET `IsActive` = 0 WHERE `ID` = $auction");
		if (empty($result))
		{
			throw new Exception('Failed to end auction');
		}
	}

	public static function setWinner($data)
	{
		TokenHelper::assertToken();
		$agent = Agent::getAgentFromToken(TokenHelper::getToken());
		if (!$agent->isAuctioneer())
		{
			throw new Exception('You can not administrate auctions');
		}

		$db = Database::getConnection();
		$auction = intval($data['Auction']);
		$winner = $db->escape_string($data['Username']);
		$cost = $data['Cost'] ? doubleval($data['Cost']) : 0;

		try
		{
			$db->begin_transaction();

			if (empty($winner))
			{
				// Delete auction if iteration >= 3
				$result = $db->query("DELETE FROM `Auction` WHERE `ID` = $auction AND `Iteration` >= 3");
				if (empty($result))
				{
					throw new Exception('Failed to remove auction');
				}
			}
			else
			{
				// Transfer requests
				$result = $db->query("UPDATE `TransportRequest`
				SET `Owner` = '$winner', `Cost` = $cost
				WHERE `ID` IN (SELECT `TransportRequest`
					FROM `AuctionRequests`
					WHERE `Auction` = $auction)");
				if (empty($result))
				{
					throw new Exception('Failed to update transport requests');
				}

				// Delete auction
				$result = $db->query("DELETE FROM `Auction` WHERE `ID` = $auction");
				if (empty($result))
				{
					throw new Exception('Failed to remove auction');
				}
			}

			$db->commit();
		}
		catch (\Throwable $ex)
		{
			$db->rollback();
			throw $ex;
		}
	}
}