<?php

// Set response mime type

use CCN\Route\Handler;

header('Content-type: application/json');

require __DIR__ . '/Util/autoload.php';

try
{
	// Validate request format
	$method = $_SERVER['REQUEST_METHOD'];
	$body = file_get_contents('php://input');
	$body = json_decode($body);

	if ($body === false)
	{
		throw new Exception(json_last_error_msg(), json_last_error());
	}

	// Get request handler
	$handler = Handler::get();
	$result = $handler->execute($body['Cmd'], $body['Data']);

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
	exit();
}