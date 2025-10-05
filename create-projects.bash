#!/usr/bin/env bash

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=assets-service \
--package-name=pallares.gameassetmarketplace.assets \
--groupId=pallares.gameassetmarketplace.assets \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
assets-service

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=payments-service \
--package-name=pallares.gameassetmarketplace.payments \
--groupId=pallares.gameassetmarketplace.payments \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
payments-service

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=assets-transactions-service \
--package-name=pallares.gameassetmarketplace.assets-transactions \
--groupId=pallares.gameassetmarketplace.assets-transactions \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
assets-transactions-service

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=pallares.gameassetmarketplace.apigateway \
--groupId=pallares.gameassetmarketplace.apigateway \
--dependencies=web,webflux,validation,hateoas \
--version=1.0.0-SNAPSHOT \
api-gateway

spring init \
--boot-version=3.4.4 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=users-service \
--package-name=pallares.gameassetmarketplace.users \
--groupId=pallares.gameassetmarketplace.users \
--dependencies=web,webflux,validation \
--version=1.0.0-SNAPSHOT \
users-service

