<?php

namespace CCN;

use CCN\Util\TokenHelper;
use Exception;

class Agent
{
	/**
	 * Registers a new agent in the database
	 * @param  array $data
	 * @return Agent
	 */
	public static function register($data)
	{
		$db = Database::getConnection();
		$username = $db->escape_string($data['Username']);
		$password = $db->escape_string($data['Password']);
		$name = $db->escape_string($data['Name']);
		$isAuctioneer = intval(boolval($data['IsAuctioneer']));
		$minProfit = array_key_exists('MinProfit', $data) ? doubleval($data['MinProfit']) : 0;
		$maxProfit = array_key_exists('MaxProfit', $data) ? doubleval($data['MaxProfit']) : 0;
		$depotLat = $isAuctioneer ? 0 : floatval($data['DepotLat']);
		$depotLon = $isAuctioneer ? 0 : floatval($data['DepotLon']);
		$pickup = $isAuctioneer ? 0 : floatval($data['PickupBaserate']);
		$travelExtern = $isAuctioneer ? 0 : floatval($data['TravelCostPerKM']);
		$load = $isAuctioneer ? 0 : floatval($data['LoadBaserate']);
		$travelIntern = $isAuctioneer ? 0 : floatval($data['InternalTravelCostPerKM']);
		$hash = password_hash($password, PASSWORD_DEFAULT);

		$result = $db->query("SELECT COUNT(*) AS `Count` FROM `Agent` WHERE `Username` = '$username'");
		if ($result === false)
		{
			throw new \Exception($db->error);
		}

		if (($row = $result->fetch_assoc()) && $row['Count'] != 0)
		{
			throw new \Exception("Username $username is alread used");
		}

		$result = $db->query("INSERT INTO `Agent` (`Username`, `Name`, `Password`, `IsAuctioneer`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM`) VALUES ('$username', '$name', '$hash', $isAuctioneer, $minProfit, $maxProfit, $depotLat, $depotLon, $pickup, $travelExtern, $load, $travelIntern)");
		if ($result === false)
		{
			throw new \Exception($db->error);
		}

		return new Agent($username, $name, $isAuctioneer);
	}

	/**
	 * @param  array $data
	 * @return string session token
	 */
	public static function login($data)
	{
		$db = Database::getConnection();

		try
		{
			$db->begin_transaction();

			$username = $db->escape_string($data['Username']);
			$password = $db->escape_string($data['Password']);

			$result = $db->query("SELECT `Username`, `Name`, `Password`, `IsAuctioneer`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM` FROM `Agent` WHERE `Username` LIKE '$username'");
			if ($result === false)
			{
				throw new \Exception($db->error);
			}
			$data = $result->fetch_assoc();
			if (!$data)
			{
				throw new \Exception("$username not found");
			}
			if (!password_verify($password, $data['Password']))
			{
				throw new \Exception("Invalid password");
			}

			// Clean data
			unset($data['Password']);
			$data['IsAuctioneer'] = boolval($data['IsAuctioneer']);

			$result = $db->query("SELECT * FROM `Session` WHERE `Agent` = '$username'");
			if ($row = $result->fetch_assoc())
			{
				$token = $row['Token'];
				$db->query("UPDATE `Session` SET `Expiration` = UTC_TIMESTAMP() + INTERVAL 2 HOUR WHERE `Token` = '$token'");
			}
			else
			{
				$token = md5(rand());
				$db->query("INSERT INTO `Session` (`Token`, `Agent`, `Expiration`) VALUES ('$token', '$username', UTC_TIMESTAMP() + INTERVAL 2 HOUR)");
			}

			$db->commit();
	
			return [
				'Token' => $token,
				'Agent' => $data,
			];
		}
		catch (\Exception $ex)
		{
			$db->rollback();
			throw $ex;
		}
	}

	/** @return array */
	public static function getAgent($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$username = $db->escape_string($data['Username']);
		$result = $db->query("SELECT `Username`, `Name`, `IsAuctioneer`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM` FROM `Agent` WHERE `Username` = '$username'");
		if ($result === false || $result->num_rows == 0)
		{
			throw new \Exception("$username not found");
		}

		$row = $result->fetch_assoc();
		$isAuctioneer = boolval($row['IsAuctioneer']);

		$agent = [
			'Username' => $username = $row['Username'],
			'Name' => $row['Name'],
			'IsAuctioneer' => $isAuctioneer,
		];

		if (!$isAuctioneer)
		{
			$agent['MinProfit'] = $row['MinProfit'];
			$agent['MaxProfit'] = $row['MaxProfit'];
			$agent['DepotLat'] = $row['DepotLat'];
			$agent['DepotLon'] = $row['DepotLon'];
			$agent['PickupBaserate'] = $row['PickupBaserate'];
			$agent['TravelCostPerKM'] = $row['TravelCostPerKM'];
			$agent['LoadBaserate'] = $row['LoadBaserate'];
			$agent['InternalTravelCostPerKM'] = $row['InternalTravelCostPerKM'];
		}

		return $agent;
	}

	/** @return array List of agents */
	public static function getAgents($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$result = $db->query("SELECT Username, Name AS UserDisplayname, IsAuctioneer, MinProfit, MaxProfit, DepotLat, DepotLon, PickupBaserate, TravelCostPerKM, LoadBaserate, InternalTravelCostPerKM FROM `Agent`");
		$agents = [];
		while ($row = $result->fetch_assoc())
		{
			$username = $row['Username'];
			$isAuctioneer = boolval($row['IsAuctioneer']);

			if (empty($agents[$username]))
			{
				$agents[$username] = [
					'Username' => $username,
					'Name' => $row['UserDisplayname'],
					'IsAuctioneer' => $isAuctioneer,
				];

				if (!$isAuctioneer)
				{
					$agents[$username]['MinProfit'] = $row['MinProfit'];
					$agents[$username]['MaxProfit'] = $row['MaxProfit'];
					$agents[$username]['DepotLat'] = $row['DepotLat'];
					$agents[$username]['DepotLon'] = $row['DepotLon'];
					$agents[$username]['PickupBaserate'] = $row['PickupBaserate'];
					$agents[$username]['TravelCostPerKM'] = $row['TravelCostPerKM'];
					$agents[$username]['LoadBaserate'] = $row['LoadBaserate'];
					$agents[$username]['InternalTravelCostPerKM'] = $row['InternalTravelCostPerKM'];
				}
			}
		}
		return array_values($agents);
	}

	/** @return array List of auctioneer agents */
	public static function getAuctioneers($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$result = $db->query("SELECT Username, Name AS UserDisplayname FROM `Agent` WHERE IsAuctioneer");
		$agents = [];
		while ($row = $result->fetch_assoc())
		{
			$username = $row['Username'];

			if (empty($agents[$username]))
			{
				$agents[$username] = [
					'Username' => $username,
					'Name' => $row['UserDisplayname'],
					'IsAuctioneer' => true,
				];
			}
		}
		return array_values($agents);
	}

	/** @return array List of carrier agents */
	public static function getCarriers($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$result = $db->query("SELECT `Username`, `Name`, `IsAuctioneer`, `MinProfit`, `MaxProfit`, `DepotLat`, `DepotLon`, `PickupBaserate`, `TravelCostPerKM`, `LoadBaserate`, `InternalTravelCostPerKM` FROM `Agent` WHERE NOT `IsAuctioneer`");
		$agents = [];
		while ($row = $result->fetch_assoc())
		{
			$agents[] = $row;
		}
		return $agents;
	}

	public static function setMinProfit($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$username = $db->escape_string($data['Username']);
		$price = doubleval($data['Price']);
		$result = $db->query("UPDATE `Agent` SET `MinProfit` = $price WHERE `Username` = '$username'");
	}

	public static function setMaxProfit($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$username = $db->escape_string($data['Username']);
		$price = doubleval($data['Price']);
		$result = $db->query("UPDATE `Agent` SET `MaxProfit` = $price WHERE `Username` = '$username'");
	}

	/**
	 * @param  string $token Session token of an auctioneer agent
	 * @return Agent
	 */
	public static function getAgentFromToken($token)
	{
		$db = Database::getConnection();
		$token = $db->escape_string($token);
		$result = $db->query("SELECT * FROM `Session` s LEFT JOIN `Agent` a ON s.Agent = a.Username WHERE s.Token = '$token'");
		if (empty($result))
		{
			throw new Exception('Token not matched to an agent');
		}
		$row = $result->fetch_assoc();
		$username = $row['Username'];
		$name = $row['Name'];
		$isAuctioneer = $row['IsAuctioneer'];

		return new Agent($username, $name, $isAuctioneer);
	}

	/**
	 * Only call this constructor if you know this user exists!
	 * @param string $username The unique username used to identify the agent
	 * @param string $name A display name for the Gui
	 * @param bool $isAuctioneer True if this agent is the auctioneer
	 */
	function __construct($username, $name, $isAuctioneer) //, $depotLat, $depotLon)
	{
		//  * @param float $depotLat Latitude of the depot
		//  * @param float $depotLon Longitude of the depot
		$this->username = $username;
		$this->name = $name;
		$this->isAuctioneer = $isAuctioneer;
		// $this->depotLat = $depotLat;
		// $this->depotLon = $depotLon;
	}

	public function getUsername()
	{
		return $this->username;
	}

	public function isAuctioneer()
	{
		return $this->isAuctioneer;
	}

	/** @var string $username */
	private $username;
	/** @var string $name */
	private $name;
	/** @var bool $isAuctioneer */
	private $isAuctioneer;
	// /** @var string|float $depotLat */
	// private $depotLat;
	// /** @var string|float $depotLon */
	// private $depotLon;
}