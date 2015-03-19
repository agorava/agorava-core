#Agorava Core


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
Agorava core relies on [Agorava Parent](https://github.com/agorava/parent) so you should have installed this resource to your
 local maven repo
(Agorava is not available in a public maven repo yet). After that you can build (and install in your local maven repo) Agorava Core with the following command

    mvn clean install

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.agorava/agorava-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.agorava/agorava-core) 
