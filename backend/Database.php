<?php

namespace CCN;

use Exception;

class Database
{
	/** @return \mysqli Database connection */
	public static function getConnection()
	{
		// Create database connection
		$host = 'localhost';
		$user = 'ckh1694';
		$passwd = 'a9xbgEsJz&fT';
		
		ini_set("mysql.default_socket", "/var/lib/mysql/mysql.sock");
		$connection = mysqli_connect($host, $user, $passwd, 'ckh1694');
		if (!$connection)
		{
			throw new Exception('Could not connect to database server');
		}
		
		return $connection;
	}
}