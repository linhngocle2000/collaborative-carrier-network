<?php

namespace CCN\Route;

use Exception;

class Handler
{
	/** @return Handler */
	public static function get()
	{
		if (empty(Handler::$handler))
		{
			Handler::$handler = new Handler();
		}

		return Handler::$handler;
	}
	private static $handler;

	/**
	 * Register a command to be served by this application
	 * @param string $command
	 * @param callable $callback
	 */
	public function register($command, $callback)
	{
		if (!is_callable($callback))
		{
			throw new Exception("Route callback is not a callable");
		}

		if (isset($this->commands[$command]))
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
		if (empty($this->commands[$command]))
		{
			throw new Exception("Not found");
		}

		$this->commands[$command]($data);
	}

	private function __construct()
	{
		$this->commands = [];
	}

	/** @var array $commands A mapping of commands to callable objects */
	private $commands;
}