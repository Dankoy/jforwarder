# jforwarder

[![GitHub Release](https://img.shields.io/github/v/release/dankoy/jforwarder)](https://github.com/Dankoy/jforwarder/releases/latest)
![GitHub Release Date](https://img.shields.io/github/release-date/dankoy/jforwarder)
![Commits since last release](https://img.shields.io/github/commits-since/Dankoy/jforwarder/latest/main)

![GitHub tag checks state](https://img.shields.io/github/checks-status/dankoy/jforwarder/main)
![GitHub CI](https://github.com/dankoy/jforwarder/actions/workflows/test.yml/badge.svg)
![Dependencies](https://img.shields.io/librariesio/github/dankoy/jforwarder)

![Hits](https://hitscounter.dev/api/hit?url=https%3A%2F%2Fgithub.com%2FDankoy%2Fjforwarder&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)

![GitHub search hit counter](https://img.shields.io/github/search/dankoy/jforwarder/jforwarder)
![GitHub search hit counter](https://img.shields.io/github/search/dankoy/jforwarder/coub_forwarder_bot)
![GitHub search hit counter](https://img.shields.io/github/search/dankoy/jforwarder/coub)

# Trademark disclaimer

Coub and the Coub logo are trademarks of Coub.com Ltd and its affiliates. All rights reserved.    
Coubs are property of Coub.com Ltd and their respective owners.    
I do not own anything affiliated with Coub.com.

## Bot link

https://t.me/coub_forwarder_bot

# Purpose

The reason for creation of this project is to have access to coubs in telegram private or group
chats. Without registration and sms.

# Features

1) Subscription on channel
2) Subscription on tag
3) Subscription on community

Subscriptions can be created for:
- individual private chats
- supergroups
- separate topics in supergroups

## Usage

User can subscribe to different coub communities and sections, by tags or directly to channels in
telegram bot. Then periodically bot will send new coubs to subscribed user. Bot sends only coubs
that has been published after last sent coub to chat.

### Example of making community subscription

1) To get all available communities and its sections:    
   ```/communities```

   > Available communities:    
   community: animals-pets    
   sections: monthly, quarter, half, daily, fresh, rising, weekly    
   ... truncated ...

2) To subscribe to community or tag:    
   ```/subscribe community animals-pets weekly```    
   ```/subscribe tag cars popular```    
   ```/subscribe channel meteoralp most_recent```

3) Bot will periodically send links to coubs for every subscription user has.

   Example of message:
   > community #animals-pets #weekly    
   https://coub.com/view/3ca3fu

4) Show user active subscriptions: ```/my_subscriptions```
   > Community subscriptions:    
   1: animals-pets weekly

> [!NOTE]
> If you have questions about running this utility then feel free to ask it in [github discusstions](https://github.com/Dankoy/jforwarder/discussions) or in [discord server](https://discord.gg/kDTumB6fEk)

## Where to find channel identification?

Open coub channel in browser. Find the last word in URL. Use it.

![img.jpg](img.jpg)

## Stack

1. Java 21
2. Spring boot 3.4.0
3. Spring Integration
4. Spring Cloud Netflix Eureka
5. Spring Cloud Loadbalancer
6. Spring Cloud Gateway
7. Spring Data Jpa
8. JOOQ
9. Open feign
10. Caffeine Cache
11. PostgreSQL
12. Flyway
13. Liquibase
14. Micrometer tracing
15. Zipkin reporter brave
16. Zipkin
17. Docker compose
18. Telegrambots
19. Resilience4j
20. Kafka
21. Kafka Schema Registry
22. Protobuf
23. Kubernetes

## Monitoring stack

1. Prometheus
2. Grafana
3. Prometheus node exporter
4. Prometheus postgres exporter
5. Loki (logs)
6. Mimir + minio (metrics storage)
7. cAdvisor (metrics)

## Microservice architecture

Microservice communication schema:    
![drawio](jforwarder.drawio.svg)

Microservice monitoring schema:    
![drawio](jforwarder-monitoring.drawio.svg)

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
TELEGRAM_CHAT_SERVICE_POSTGRES_CONTAINER_URL=jdbc:postgresql://container_name:5432/db
TELEGRAM_CHAT_SERVICE_POSTGRES_CONTAINER_USER=user
TELEGRAM_CHAT_SERVICE_POSTGRES_CONTAINER_PASSWORD=pass
TELEGRAM_CHAT_SERVICE_POSTGRES_CONTAINER_DB=db
TELEGRAM_CHAT_SERVICE_JASYPT_MASTER_PASSWORD=pass
```

Variable **JAR_VERSION** is used only when building through command line docker compose build or
docker build

**JAR_VERSION** in has to be equal the project version in **build.gradle** file in root folder

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
docker compose up -d
```

### Monitoring

For monitoring created new docker compose project [monitoring](/monitoring)

#### Add .env file in monitoring folder

```
POSTGRES_CONTAINER_PASSWORD=password
POSTGRES_CONTAINER_USER=user
DATA_SOURCE_NAME="postgresql://${POSTGRES_CONTAINER_USER}:${POSTGRES_CONTAINER_PASSWORD}@<container-name>:<port>/?sslmode=disable"
GF_SECURITY_ADMIN_USER=user
GF_SECURITY_ADMIN_PASSWORD=password
```

#### Run monitoring

```shell
cd monitoring && docker compose up -d
```
