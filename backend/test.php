<?php

$host = 'localhost';
$user = 'ckh1694';
$passwd = 'a9xbgEsJz&fT';

ini_set("mysql.default_socket", "/var/lib/mysql/mysql.sock");
$connection = mysqli_connect($host, $user, $passwd, 'ckh1694');
$result = $connection->query("SELECT * FROM `Agent`");
while ($row = $result->fetch_assoc())
{
	echo "Agent " . $row['ID'] . ", Name: " . $row['Name'] . "<br/>";
}

// try
// {
// 	$pdo = new PDO('mysql:host=127.0.0.1;dbname=ckh1694', 'ckh1694', 'a9xbgEsJz&fT', [
// 	// $pdo = new PDO('mysql:default_socket=/var/lib/mysql/mysql.sock;mysql:host=localhost;dbname=ckh1694', 'ckh1694', 'a9xbgEsJz&fT', [
// 			PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
// 			PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
// 			PDO::MYSQL_ATTR_INIT_COMMAND => "SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci"
// 	]);

// 	$stm = $pdo->prepare("SELECT * FROM `Agent`");
// 	$stm->execute();

// 	while ($row = $stm->fetch())
// 	{
// 		echo "Agent " . $row['ID'] . ", Name: " . $row['Name'] . "<br/>";
// 	}
// }
// catch (\Throwable $ex)
// {
// 	echo $ex->getMessage();
// 	echo $ex->getTraceAsString();
// }