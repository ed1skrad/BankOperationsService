# Тестовое задание Сервис для банковских опреаций
Это приложение, написанное с использованием следующих технологий:

[![Docker](https://github.com/ed1skrad/BankOperationsService/actions/workflows/pipeline.yaml/badge.svg)](https://github.com/ed1skrad/BankOperationsService/actions/workflows/pipeline.yaml)

- **Java 17**
- **Spring Web**
- **Spring Data Jpa**
- **PostgreSQL**
- **Spring Security**
- **AOP**

## Описание

Сервис содержит в себе базовую логику для работы банковского приложения

## Качество кода

В процессе разработки использовался SonarCloud для проверки качества написания кода.

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=ed1skrad_BankOperationsService)](https://sonarcloud.io/summary/new_code?id=ed1skrad_BankOperationsService)

## Schema
![db](https://github.com/ed1skrad/BankOperationsService/blob/main/image/db.jpg)

## Swagger
![swagger](https://github.com/ed1skrad/BankOperationsService/blob/main/image/swagger.jpg)

## Dockerized
How to run?
docker-compose up

## env
- **POSTGRES_USER=example**
- **POSTGRES_PASSWORD=example**
- **POSTGRES_DB=bankdb**
- **SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/bankdb**
- **SPRING_DATASOURCE_USERNAME=example**
- **SPRING_DATASOURCE_PASSWORD=example**

## docs
localhost:port/swagger-ui/index.html
