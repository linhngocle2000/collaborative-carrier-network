<?php

namespace CCN;

use Exception;

class Agent
{
	/**
	 * Registers a new agent in the database
	 * @param  string $username A unique name used to identify the agent
	 * @param  string $name A display name for the Gui
	 * @param  string $password
	 * @return Agent
	 */
	public static function register($username, $name, $password)
	{
		$db = Database::getConnection();
		$username = $db->escape_string($username);
		$name = $db->escape_string($name);
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

		$result = $db->query("INSERT INTO `Agent` (`Username`, `Name`, `Password`) VALUES ('$username', '$name', '$hash')");
		if ($result === false)
		{
			throw new \Exception($db->error);
		}

		return new Agent($username, $name);
	}

	/**
	 * @param  string $username
	 * @param  string $password
	 * @return string session token
	 */
	public static function login($username, $password)
	{
		$db = Database::getConnection();

		try
		{
			$db->begin_transaction();

			$username = $db->escape_string($username);
			$result = $db->query("SELECT `Username`, `Name`, `Password` FROM `Agent` WHERE `Username` LIKE '$username'");
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
			$data['Password'] = null;

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
	
			return $token;
		}
		catch (\Exception $ex)
		{
			$db->rollback();
			throw $ex;
		}
	}

	/**
	 * Only call this constructor if you know this user exists!
	 * @param string $username The unique username used to identify the agent
	 * @param string $name A display name for the Gui
	 */
	function __construct($username, $name)
	{
		$this->username = $username;
		$this->name = $name;
	}

	/** @var string $username */
	private $username;
	/** @var string $name */
	private $name;
}