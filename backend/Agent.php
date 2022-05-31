<?php

namespace CCN;

use CCN\Util\TokenHelper;

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
		$vehicle = $db->escape_string($data['Vehicle']);
		$depotLat = floatval($data['DepotLat']);
		$depotLon = floatval($data['DepotLon']);
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

		$result = $db->query("INSERT INTO `Agent` (`Username`, `Name`, `Password`, `IsAuctioneer`, `Vehicle`, `DepotLat`, `DepotLon`) VALUES ('$username', '$name', '$hash', $isAuctioneer, '$vehicle', $depotLat, $depotLon)");
		if ($result === false)
		{
			throw new \Exception($db->error);
		}

		return new Agent($username, $name, $isAuctioneer, $vehicle, $depotLat, $depotLon);
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

			$result = $db->query("SELECT `Username`, `Name`, `Password`, `IsAuctioneer`, `Vehicle`, `DepotLat`, `DepotLon` FROM `Agent` WHERE `Username` LIKE '$username'");
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

	/** @return array List of agents */
	public static function getAgents($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$result = $db->query("SELECT a.Username, a.Name AS UserDisplayname, a.IsAuctioneer, a.Vehicle, a.DepotLat, a.DepotLon, ac.ID AS AuctionID, ac.Name AS AuctionName FROM `Agent` a LEFT JOIN `Auction` ac ON a.Username = ac.Auctioneer");
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

				if ($isAuctioneer)
				{
					$agents[$username]['Auctions'] = [];
				}
				else
				{
					$agents[$username]['Vehicle'] = $row['Vehicle'];
					$agents[$username]['DepotLat'] = $row['DepotLat'];
					$agents[$username]['DepotLon'] = $row['DepotLon'];
				}
			}

			if ($isAuctioneer && isset($row['AuctionID']))
			{
				$agents[$username]['Auctions'][] = [
					'ID' => $row['AuctionID'],
					'Name' => $row['AuctionName'],
					'Auctioneer' => $username,
				];
			}
		}
		return array_values($agents);
	}

	/** @return array List of auctioneer agents */
	public static function getAuctioneers($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$result = $db->query("SELECT a.Username, a.Name AS UserDisplayname, ac.ID AS AuctionID, ac.Name AS AuctionName FROM `Agent` a LEFT JOIN `Auction` ac ON a.Username = ac.Auctioneer WHERE a.IsAuctioneer");
		$agents = [];
		while ($row = $result->fetch_assoc())
		{
			$username = $row['Username'];

			if (empty($agents[$username]))
			{
				$agents[$username] = [
					'Username' => $username,
					'Name' => $row['UserDisplayname'],
					'Auctions' => [],
				];
			}

			if (isset($row['AuctionID']))
			{
				$agents[$username]['Auctions'][] = [
					'ID' => $row['AuctionID'],
					'Name' => $row['AuctionName'],
					'Auctioneer' => $username,
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
		$result = $db->query("SELECT `Username`, `Name`, `Vehicle`, `DepotLat`, `DepotLon` FROM `Agent` WHERE NOT `IsAuctioneer`");
		$agents = [];
		while ($row = $result->fetch_assoc())
		{
			$agents[] = $row;
		}
		return $agents;
	}

	/**
	 * Only call this constructor if you know this user exists!
	 * @param string $username The unique username used to identify the agent
	 * @param string $name A display name for the Gui
	 * @param bool $isAuctioneer True if this agent is the auctioneer
	 * @param string $vehicle Name of the ship
	 * @param float $depotLat Latitude of the depot
	 * @param float $depotLon Longitude of the depot
	 */
	function __construct($username, $name, $isAuctioneer, $vehicle, $depotLat, $depotLon)
	{
		$this->username = $username;
		$this->name = $name;
		$this->isAuctioneer = $isAuctioneer;
		$this->vehicle = $vehicle;
		$this->depotLat = $depotLat;
		$this->depotLon = $depotLon;
	}

	/** @var string $username */
	private $username;
	/** @var string $name */
	private $name;
	/** @var bool $isAuctioneer */
	private $isAuctioneer;
	/** @var string $vehicle */
	private $vehicle;
	/** @var string|float $depotLat */
	private $depotLat;
	/** @var string|float $depotLon */
	private $depotLon;
}