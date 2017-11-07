# PhoneBook

Software used:

JDK 8

WildFly 11 as the Application Server (http://wildfly.org/downloads/)

Maven 3

PostgreSQL 10 (https://www.postgresql.org/download/) or another DBMS of your choosing

IntelliJ Ultimate 2017 for development but the project should be run fine just with Maven

Postman to test the endpoints

Instuctions:

Configure a Datasource of your choice in WildFly, in this example it's name is: java:/PostgresDSUbi
(optionally you can use the default datasource of WildFly that uses H2:mem - java:jboss/datasources/ExampleDS but beware that any this datasource only stays in memory and never saves to disk)

Endpoints:

