#Agorava Core


##Introduction
Agorava Provides CDI Beans and extensions to interact with major social network (SN). 

Agorava core contains SN independant services like

+ OAuth connectors to authentify with an OAuth providers
+ Support for generic Authentication and user profile
+ Support for multi-account (multi SN and multi session for the same SN)

Core also contains provides implemntations for these service.
The only implementation is based on CDI right now.

##CDI implementation
It is independent of CDI implementation and fully portable between
Java EE 6 and Servlet environments enhanced with CDI. It can be also used 
with CDI in JSE (desktop application). It is build on top of [scribe-java
from fernandezpablo85](https://github.com/fernandezpablo85/scribe-java)

For more information, see the [Agorava Website](http://agorava.org).

##Building
Agorava core relies on [Agorava Parent](https://github.com/agorava/parent) so you should have intalled this resource to your local maven repo
(Agorava is not availbale in a public maven repo yet). After that you can build (and install in your local maven repo) Agorava Core with the following command

    mvn clean install

##Usage
You'll need at least one SN module to use Agorava. Right now there's only one module for [Twitter](https://github.com/agorava/twitter)