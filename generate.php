<?php

$letters = [ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L' ];
$path = __DIR__ . "/output.txt";
$content = '';

foreach ($letters as $letter) {
	for ($i = 0; $i < 10; $i++) {
		$lat1 = random_int(0, 200);
		$lat2 = random_int(0, 200);
		$lat3 = random_int(0, 200);
		$lat4 = random_int(0, 200);
		$content .= "\"INSERT INTO `TransportRequest`(`Owner`, `PickupLat`, `PickupLon`, `DeliveryLat`, `DeliveryLon`) VALUES ('agent$letter', $lat1, $lat2, $lat3, $lat4)\",\n";
	}
}

file_put_contents($path, $content);