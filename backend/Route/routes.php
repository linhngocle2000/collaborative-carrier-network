<?php

use CCN\Agent;
use CCN\Auction;
use CCN\Database;
use CCN\Route\Handler;
use CCN\TransportRequest;
use CCN\Util\TokenHelper;

$handler = Handler::get();

// Agent routes
$handler->register('register', [Agent::class, 'register']);
$handler->register('login', [Agent::class, 'login']);
$handler->register('getAgent', [Agent::class, 'getAgent']);
$handler->register('getAgents', [Agent::class, 'getAgents']);
$handler->register('getAuctioneers', [Agent::class, 'getAuctioneers']);
$handler->register('getCarriers', [Agent::class, 'getCarriers']);

// Transport request routes
$handler->register('addRequest', [TransportRequest::class, 'addRequest']);
$handler->register('getRequests', [TransportRequest::class, 'getRequests']);
$handler->register('getRequestsOfAgent', [TransportRequest::class, 'getRequestsOfAgent']);
$handler->register('getRequestsOfAuction', [TransportRequest::class, 'getRequestsOfAuction']);

// Auction routes
$handler->register('addAuction', [Auction::class, 'addAuction']);
$handler->register('getAuctions', [Auction::class, 'getAuctions']);
$handler->register('addRequestToAuction', [Auction::class, 'addTransportRequest']);