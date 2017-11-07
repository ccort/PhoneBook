# PhoneBook

## Software used:

**[JDK 8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)**

**[WildFly 11](http://wildfly.org/downloads/)** as the Application Server 

**[Apache Maven 3.5.2](https://maven.apache.org/download.cgi)** 

**[JDBC Connector 9.4.1212 JDBC 42](https://jdbc.postgresql.org/download.html)**

**[PostgreSQL 10](https://www.postgresql.org/download/)** or another DBMS of your choice

**[IntelliJ Ultimate 2017](https://www.jetbrains.com/idea/download/)**  *(Optional)*  for development but the project should run fine just with Maven

**[Postman](https://www.getpostman.com/)** for endoint testing

## Instructions:

Configure a Datasource of your choice in WildFly; in this example its name is: **java:/PostgresDSUbi**
(optionally you can use the default datasource of WildFly that uses **H2:mem - java:jboss/datasources/ExampleDS** but beware that this type of datasource is volatile; it only stays in the memory and is never saved to the disk)

## Endpoints:

### To run it:

Make sure that WildFly is executing, the datasource is configured and that the deployment is performed with the following args: `mvn wildfly:deploy -DskipTests=True` (tests are configured to run in IntelliJ only for the moment)

### Undeploy: Type:  `mvn wildfly:undeploy`
