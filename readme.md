#Agorava Core

[![Circle CI](https://circleci.com/gh/agorava/agorava-core.svg?style=svg)](https://circleci.com/gh/agorava/agorava-core) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.agorava/agorava-core-parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.agorava/agorava-core-parent) 
[![License](http://img.shields.io/badge/license-Apache2-red.svg)](http://opensource.org/licenses/apache-2.0)

##Introduction
Agorava Provides CDI Beans and extensions to interact with major social media (SM).

Agorava core contains SN independent services like

+ OAuth connectors to authenticate with an OAuth providers
+ Support for generic Authentication and user profile
+ Support for multi-account (multi SM and multi session for the same SM)

Core also contains provides implementations for these service.
The only implementation is based on CDI right now.

##CDI implementation
It is independent of CDI implementation and fully portable between
Java EE 6 and Servlet environments enhanced with CDI. It can be also used 
with CDI in JSE (desktop application).

For more information, see the [Agorava Website](http://agorava.org).

##Building
Agorava core relies on [Agorava Parent](https://github.com/agorava/agorava-parent)

It requires Apache Maven (available from http://maven.apache.org/) 
Version 3.0.3 or later to build. You'll need to launch the following command in the root of the project

    mvn clean install
