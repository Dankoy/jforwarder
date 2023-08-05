# jforwarder

![GitHub CI](https://github.com/dankoy/jforwarder/actions/workflows/main.yml/badge.svg)    
[![HitCount](https://hits.dwyl.com/dankoy/jforwarder.svg?style=flat)](http://hits.dwyl.com/dankoy/jforwarder)

# Trademark disclaimer

Coub and the Coub logo are trademarks of Coub.com Ltd and its affiliates. All rights reserved.    
Coubs are property of Coub.com Ltd and their respective owners.    
I do not own anything affiliated with Coub.com.

## Bot link

https://t.me/coub_forwarder_bot

# Purpose

The reason for creation of this project is to have access to coubs in telegram private or group
chats.

## Usage

User can subscribe to different coub communities and sections or by tags in telegram bot. Then
periodically bot will send new coubs to subscribed user. Bot sends only coubs that has been
published after last sent coub to chat.

## Microservice architecture

Microservice communication schema:    
![drawio](jforwarder.drawio.svg)

## Build

Create .env file with secret variables

```
JASYPT_MASTER_PASSWORD=pass
POSTGRES_CONTAINER_PASSWORD=pass
POSTGRES_CONTAINER_DB=db
POSTGRES_CONTAINER_USER=user
POSTGRES_CONTAINER_URL=jdbc:postgresql://container_name:port/db
TELEGRAM_BOT_NAME=name
TELEGRAM_BOT_API_TOKEN=token
JAR_VERSION=x.x.x-SNAPSHOT
DOCKER_HUB_USER=user
```

Variable **JAR_VERSION** is used only when building through command line docker compose build or
docker build

**JAR_VERSION** in has to be equal the project version in **build.gradle** file in root folder

#### Build using docker compose gradle plugin

Builds every microservice

```shell
./gradlew composeBuild
```

#### Build using docker compose command line

Builds every microservice

```shell
docker compose build 
```

#### Build using docker buildx command line

Have to run this command for every dockerfile manually

```shell
docker buildx build --platform linux/amd64,linux/arm64 --build-arg JAR_VERSION=x.x.x-SNAPSHOT -f .\Dockerfile.{microservice}:x.x.x-SNAPSHOT .
```

#### Run compose

```shell
docker compose up
```