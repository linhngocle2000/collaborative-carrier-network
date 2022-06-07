<?php

namespace CCN\Util;

use CCN\Database;
use Exception;

class TokenHelper
{
	public static function setSession($token)
	{
		TokenHelper::$token = $token;
	}

	public static function assertToken()
	{
		if (empty(TokenHelper::$token))
		{
			throw new Exception("No token specified");
		}

		$db = Database::getConnection();
		$result = $db->query("SELECT COUNT(*) AS `Count` FROM `Session` WHERE `Token` = '" . TokenHelper::$token . "'");
		if ($result === false || !($row = $result->fetch_assoc()) || $row['Count'] == 0)
		{
			throw new Exception("Not authorized");
		}

		$result = $db->query("SELECT COUNT(*) AS `Count` FROM `Session` WHERE `Token` = '" . TokenHelper::$token . "' AND `Expiration` > UTC_TIMESTAMP()");
		if ($result === false|| !($row = $result->fetch_assoc()) || $row['Count'] == 0)
		{
			throw new Exception("Session expired");
		}
	}

	/** @var string $token */
	private static $token;
}