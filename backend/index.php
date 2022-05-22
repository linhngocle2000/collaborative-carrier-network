<?php

// Set response mime type
header('Content-type: application/json');

// Register autoloader
spl_autoload_register(function ($class)
{
	if (str_starts_with($class, 'CCN'))
	{
		$class = substr($class, 4);
	}
	else
	{
		return false;
	}

	$file = __DIR__ . '/src/' . str_replace('\\', DIRECTORY_SEPARATOR, $class) . '.php';

	if (file_exists($file))
	{
		require $file;
		return true;
	}

	return false;
});

// Create database connection
$host = 'localhost';
$user = 'ckh1694';
$passwd = 'a9xbgEsJz&fT';

ini_set("mysql.default_socket", "/var/lib/mysql/mysql.sock");
$connection = mysqli_connect($host, $user, $passwd, 'ckh1694');

// Validate request format
$method = $_SERVER['REQUEST_METHOD'];
$body = file_get_contents('php://input');
$body = json_decode($body);

if ($body === false)
{
	http_response_code(400);
	echo json_encode([
		"success" => false,
		"error" => json_last_error_msg() 
	]);
	exit();
}

// Get request handler
echo json_encode([
	"success" => true
]);