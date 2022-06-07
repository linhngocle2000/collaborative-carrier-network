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
			throw new Exception("Not authorized");
		}

		$db = Database::getConnection();
		$result = $db->query("SELECT COUNT(*) FROM `Session` WHERE `Token` = '" . TokenHelper::$token . "'");
		if ($result === false || $result->num_rows == 0)
		{
			throw new Exception("Not authorized");
		}

		$result = $db->query("SELECT COUNT(*) FROM `Session` WHERE `Token` = '" . TokenHelper::$token . "' AND `Expiration` > UTC_TIMESTAMP()");
		if ($result === false || $result->num_rows == 0)
		{
			throw new Exception("Session expired");
		}
	}

	/** @var string $token */
	private static $token;
}