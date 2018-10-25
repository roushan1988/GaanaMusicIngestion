# TimesSubscription

    - This system stores User state information which is primarily the user info and his subscription purchase
    state in the current setup
    - It publishes User state changes to outside world via Kafka and can be fetched via API as well
    - All the relevant user state changes are driven from here
    - Payments systems closely interact with Subscription
    - SubscriptionBase has all dependencies which can be packaged as jar and used in other projects like Middleware
    This includes all Enums, Request and Response objects, relevant constants and utilities.
    - SubscriptionMain contains all main application logic to be deployed as war on tomcat servers.

## Requirements
    - Java
    - Redis
    - MySQL
    - Kafka
    - Swagger
    - Tomcat

## API and DB Details
    - APIs be found in curl.txt
    - DB tables can be found in init.sql

## Installation
    * Swagger Setup

## How To Run
     - create log directory /var/log/timesSubscription/logs/ 
       and provide user write access
     - put aplication.properties file with relevant properties in /opt/timesSubscription directory
     - put mysql.properties in /opt/timesPrime directory
  
    - package install command :- mvn clean install

## Production Machine Config:
    min instance count: 2