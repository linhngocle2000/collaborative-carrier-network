<?php

namespace CCN;

use CCN\Util\TokenHelper;

class TransportRequest
{
	/** @return int ID of new request */
	public static function addRequest($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();		
		$requests = [];
		foreach ($data as $row)
		{
			$owner = $db->escape_string($row['Agent']);
			$pickLat = floatval($row['PickupLat']);
			$pickLon = floatval($row['PickupLon']);
			$delLat = floatval($row['DeliveryLat']);
			$delLon = floatval($row['DeliveryLon']);
			$requests[] = "('$owner', 0, $pickLat, $pickLon, $delLat, $delLon)";
		}
		$valStr = implode(',', $requests);

		$result = $db->query("INSERT INTO `TransportRequest`(`Owner`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES $valStr");
		if ($result === false)
		{
			throw new \Exception($db->error);
		}

		return $db->insert_id;
	}

	/** @return array List of requests */
	public static function getRequests($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();

		$result = $db->query("SELECT DISTINCT `ID`, `Owner`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`, `Auction` IS NOT NULL AS `IsInAuction`
			FROM `TransportRequest`
			LEFT JOIN `AuctionRequests` ON `TransportRequest`.ID = `AuctionRequests`.TransportRequest");
		if ($result === false)
		{
			throw new \Exception($db->error);
		}

		$requests = [];
		while ($row = $result->fetch_assoc())
		{
			$row['IsInAuction'] = boolval($row['IsInAuction']);
			$requests[] = $row;
		}
		return $requests;
	}

	/** @return array List of requests that belong to the agent */
	public static function getRequestsOfAgent($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$username = $db->escape_string($data['Agent']);
		$result = $db->query("SELECT DISTINCT `ID`, `Owner`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`, `Auction` IS NOT NULL AS `IsInAuction`
			FROM `TransportRequest`
			LEFT JOIN `AuctionRequests` ON `TransportRequest`.ID = `AuctionRequests`.TransportRequest
			WHERE `Owner` = '$username'");

		$requests = [];
		while ($row = $result->fetch_assoc())
		{
			$row['IsInAuction'] = boolval($row['IsInAuction']);
			$requests[] = $row;
		}
		return $requests;
	}

	public static function getRequestsOfAuction($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$auction = intval($data['Auction']);
		$result = $db->query("SELECT t.`ID`, t.`Owner`, t.`Cost`, t.`PickupLat`, t.`PickupLon`, t.`DeliveryLat`, t.`DeliveryLon`, true AS `IsInAuction` 
			FROM `TransportRequest` t 
			JOIN `AuctionRequests` a ON t.ID = a.TransportRequest 
			WHERE a.`Auction` = $auction");

		$requests = [];
		while ($row = $result->fetch_assoc())
		{
			$row['IsInAuction'] = boolval($row['IsInAuction']);
			$requests[] = $row;
		}
		return $requests;
	}

	public static function stashRequests($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		try
		{
			$db->begin_transaction();
			$result = $db->query("TRUNCATE `OldTransportRequest`");
			if (!$result)
			{
				throw new \Exception('Failed to truncate old transport requests');
			}
			$result = $db->query("INSERT INTO `OldTransportRequest` SELECT * FROM `TransportRequest`");
			if (!$result)
			{
				throw new \Exception('Failed to stash current transport requests');
			}
			$db->commit();
		}
		catch (\Throwable $ex)
		{
			$db->rollback();
			throw $ex;
		}
	}

	public static function getStashedRequests($data)
	{
		TokenHelper::assertToken();

		$query = "SELECT `ID`, `Owner`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon` FROM `OldTransportRequest`";
		if (is_array($data) && array_key_exists('Agent', $data))
		{
			$agent = $data['Agent'];
			$query .= " WHERE `Owner` = '$agent'";
		}

		$db = Database::getConnection();
		$result = $db->query($query);

		$requests = [];
		while ($row = $result->fetch_assoc())
		{
			$requests[] = $row;
		}
		return $requests;
	}
}