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

		$result = $db->query("SELECT COUNT(*) FROM `Agent` WHERE `Username` = $username");
		if ($result->num_rows !== 0)
		{
			throw new \Exception("$username is already used");
		}

		$result = $db->query("INSERT INTO `Agent` (`Username`, `Name`, `Password`) VALUES ($username, $name, $hash)");

		return new Agent($username, $name);
	}

	/**
	 * @param  string $username
	 * @param  string $password
	 * @return Agent
	 */
	public static function login($username, $password)
	{
		$db = Database::getConnection();
		$username = $db->escape_string($username);
		$password = password_hash($password, PASSWORD_DEFAULT);
		$result = $db->query("SELECT `Username`, `Name` FROM `Agent` WHERE `Username` = $username AND `Password` = $password");
		
		$data = $result->fetch_assoc();
		if (!$data)
		{
			throw new Exception("User $username not found");
		}

		return new Agent($data['Username'], $data['Name']);
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