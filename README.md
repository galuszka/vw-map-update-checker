# vw-map-update-checker
Tool for periodical checking for map updates for Volkswagen cars build in navigation systems

Aplication is prepared to be deployed on HEROKU platform and is optimized for free tier acconts and can run permanently there not generating additional costs.

## What it does?
Application periodically (every 60 minutes) checks if there is a new map availiable for specified navigation device model and for specified market and region combination. \
It determines new version by comparing MD5 hash of the newest map with the one specified in application properties

## Configuration
All configuration is availaible in `application.properties` file located in `src\main\resources`

All e-mail related properties has to be overwritten to work

## Running on Heroku
Application can be deployed to Heroku with all default properties. \
All properties can be overwritten by using Heroku _Config Variables_

Config Variables has to be in form of Env variables like:

`SPRING_MAIL_USERNAME` \
`SPRING_MAIL_HOST` \
`SPRING_MAIL_PASSWORD` \
`NOTIFY_EMAIL` 
