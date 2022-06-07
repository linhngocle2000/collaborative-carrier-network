<?php

namespace CCN;

use Exception;

class Database
{
	/** @var \mysqli $connection */
	private static $connection;

	/** @return \mysqli Database connection */
	public static function getConnection()
	{
		if (empty(Database::$connection))
		{
			// Create database connection
			$host = 'localhost';
			$user = 'ckh1694';
			$passwd = 'a9xbgEsJz&fT';
			
			ini_set("mysql.default_socket", "/var/lib/mysql/mysql.sock");
			mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
			Database::$connection = mysqli_connect($host, $user, $passwd, 'ckh1694');
			if (!Database::$connection)
			{
				throw new Exception('Could not connect to database server');
			}
		}
		
		return Database::$connection;
	}
}