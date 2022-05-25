<?php

// Set response mime type

use CCN\Route\Handler;
use CCN\Util\TokenHelper;

// I want all the errors
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// phpinfo();

header('Content-type: application/json');

require __DIR__ . '/Util/autoload.php';
require __DIR__ . '/Util/functions.php';

try
{
	// Validate request format
	// $method = $_SERVER['REQUEST_METHOD'];
	$body = file_get_contents('php://input');
	$body = json_decode($body, true);
	if ($body === false)
	{
		throw new Exception(json_last_error_msg(), json_last_error());
	}
	if (!array_key_exists('Cmd', $body))
	{
		throw new Exception("No command defined!");
	}

	// Set session token
	if (isset($body['Token']))
	{
		TokenHelper::setSession($body['Token']);
	}

	// Register routes
	require __DIR__ . '/Route/routes.php';

	// Handle request
	$handler = Handler::get();
	$result = $handler->execute($body['Cmd'], isset($body['Data']) ? $body['Data'] : null);

	http_response_code(200);
	echo json_encode([
		"success" => true,
		"data" => $result,
	]);
}
catch (\Exception $ex)
{
	http_response_code(400);
	echo json_encode([
		"success" => false,
		"error" => [
			"message" => $ex->getMessage(),
			"code" => $ex->getCode(),
			"stacktrace" => $ex->getTraceAsString(),
		]
	]);
}