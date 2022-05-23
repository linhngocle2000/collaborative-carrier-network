<?php

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