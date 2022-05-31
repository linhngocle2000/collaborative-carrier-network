<?php

namespace CCN\Route;

use Exception;

class Handler
{
	private static $handler;
	
	/** @return Handler */
	public static function get()
	{
		if (empty(Handler::$handler))
		{
			Handler::$handler = new Handler();
		}

		return Handler::$handler;
	}

	/**
	 * Register a command to be served by this application
	 * @param string $command
	 * @param callable $callback A function that accepts a data array as input and returns a simple type or data array (that can be parsed to json)
	 */
	public function register($command, $callback)
	{
		if (!is_callable($callback))
		{
			throw new Exception("Route callback is not a callable");
		}

		if (array_key_exists($command, $this->commands))
		{
			throw new Exception("Command is already defined");
		}

		$this->commands[$command] = $callback;
	}

	/** 
	 * Executes a command
	 * @param string $command
	 * @param array $data
	 */
	public function execute($command, $data)
	{
		if (!array_key_exists($command, $this->commands))
		{
			throw new Exception("$command not found");
		}

		return $this->commands[$command]($data);
	}

	private function __construct()
	{
		$this->commands = [];
	}

	/** @var array $commands A mapping of commands to callable objects */
	private $commands;
}