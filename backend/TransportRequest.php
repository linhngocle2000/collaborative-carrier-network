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
			$requests[] = "('$owner', NULL, 0, $pickLat, $pickLon, $delLat, $delLon)";
		}
		$valStr = implode(',', $requests);

		$result = $db->query("INSERT INTO `TransportRequest`(`Owner`, `Auction`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES $valStr");
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

		$result = $db->query("SELECT `ID`, `Owner`, `Auction`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon` FROM `TransportRequest`");
		if ($result === false)
		{
			throw new \Exception($db->error);
		}

		$requests = [];
		while ($row = $result->fetch_assoc())
		{
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
		$result = $db->query("SELECT `ID`, `Owner`, `Auction`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon` FROM `TransportRequest` WHERE `Owner` = '$username'");

		$requests = [];
		while ($row = $result->fetch_assoc())
		{
			$requests[] = $row;
		}
		return $requests;
	}

	public static function getRequestsOfAuction($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$auction = intval($data['Auction']);
		$result = $db->query("SELECT `ID`, `Owner`, `Auction`, `Cost`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon` FROM `TransportRequest` WHERE `Auction` = $auction");

		$requests = [];
		while ($row = $result->fetch_assoc())
		{
			$requests[] = $row;
		}
		return $requests;
	}
}