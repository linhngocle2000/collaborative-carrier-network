# CCN

CCN is an application presenting an auction based Collaborative Carrier Network. It is designed exclusively for small 
and medium sized carriers and harnesses the power of global optimization to deliver economically efficient solutions to
every participant in the network. In addition, carriers incur much lower costs than traditional negotiation processes.

## Table of contents

1. [Features](#features)
2. [Download](#download)
3. [Logs](#logs)
4. [User manual](#user-manual)
   1. [Registration](#registration)


## Features

- Optimize the profit of daily "pickup and delivery" transport business for all participated carriers
- Support the reassignment of transport requests, that assure an increase in profit
- An auction-based solution is used to support privacy of agents
- A graphical use interface is available to support the analytical view of solutions

## Download

Download the source code as zip file from [Gitlab](https://collaborating.tuhh.de/e16/courses/software-development/ss22/group04_coop/collaborative-carrier-network).
Unzip the downloaded file and click on _CCN.jar_ to start the application.

## Logs
When starting the application, logs are documented in the log file created for the current date. Log files are located
in the directory _logs_ and their names indicate which date are the logs referring to.

## User manual

### Registration

To register a carrier agent, choose _Register_ at the starting window. Fill out the registration form, then click on the
_Register_ button at the bottom. Here are notations on some fields in the registration form:
- **Transport requests**: Tranport requests are enclosed in <> and separated by commas. Each request is of the form ((x1,y1),(x2,y2)), where (x1,y1) is the coordinate of the pickup point and (x2,y2) is of the delivery point. The coordinates can be negative or contain a decimal point.
- **Base rate A**: Base rate to reach the pickup point. The input has to be positive and can have up to two decimal digits.
- **Base rate B**: Base rate for loading/unloading. The input has to be positive and can have up to two decimal digits.
- **Price**: Price for 1 km. The input has to be positive and can have up to two decimal digits.
- **Internal rate**: Internal rate for 1 km. The input has to be positive and can have up to two decimal digits.
- **Depot latitude/longitude**: Coordinate where the carriers start their tour. Inputs can be negative and contain a decimal point.

### Login as carrier

