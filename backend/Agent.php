<?php

namespace CCN;

use CCN\Util\TokenHelper;

class Agent
{
	/**
	 * Registers a new agent in the database
	 * @param  string $username A unique name used to identify the agent
	 * @param  string $name A display name for the Gui
	 * @param  string $password
	 * @param  bool $isAuctioneer
	 * @return Agent
	 */
	public static function register($username, $name, $password, $isAuctioneer)
	{
		$db = Database::getConnection();
		$username = $db->escape_string($username);
		$password = $db->escape_string($password);
		$name = $db->escape_string($name);
		$isAuctioneer = intval(boolval($isAuctioneer));
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

		$result = $db->query("INSERT INTO `Agent` (`Username`, `Name`, `Password`, `IsAuctioneer`) VALUES ('$username', '$name', '$hash', $isAuctioneer)");
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

			$result = $db->query("SELECT `Username`, `Name`, `Password`, `IsAuctioneer` FROM `Agent` WHERE `Username` LIKE '$username'");
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

	/**
	 * @return array List of agents
	 */
	public static function getAgents($data)
	{
		TokenHelper::assertToken();

		$db = Database::getConnection();
		$result = $db->query("SELECT a.Username, a.Name AS UserDisplayname, a.IsAuctioneer, ac.ID AS AuctionID, ac.Name AS AuctionName FROM `Agent` a LEFT JOIN `Auction` ac ON a.Username = ac.Auctioneer");
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

	/**
	 * Only call this constructor if you know this user exists!
	 * @param string $username The unique username used to identify the agent
	 * @param string $name A display name for the Gui
	 * @param bool $isAuctioneer True if this agent is the auctioneer
	 */
	function __construct($username, $name, $isAuctioneer)
	{
		$this->username = $username;
		$this->name = $name;
		$this->isAuctioneer = $isAuctioneer;
	}

	/** @var string $username */
	private $username;
	/** @var string $name */
	private $name;
	/** @var bool $isAuctioneer */
	private $isAuctioneer;

	// Route callbacks below
	
	public static function registerRoute($data)
	{
		return Agent::register($data['Username'], $data['Name'], $data['Password'], $data['IsAuctioneer']);
	}
}