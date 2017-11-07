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

### JDK & Maven Configuration
Ensure that JDK and Maven are istalled correctly by the `java -version` and `mvn -version` commands. Additionally, verify that they both exist in the system path.

### WildFly Setup
Setup the WildFly server by navigating to the WildFly \bin directory and typing the following commands:

`.\standalone.bat`, for Windows

`./standalone.sh`, for Unix-Based systems

Before logging in the server console, a management user account has to be created. This can be achieved by the following commands:

`.\add-user.bat`, for Windows

`./add-user.sh`, for Unix-Based systems

Follow the instructions present in the terminal and then navigate to the admin panel:  http://localhost:9990/console/ , from where the WildFly control panel can be accessed.

### Database Creation

Use the PostgreSQL admin application, connect to the default database with the credentials defined during the installation and create a new database. Its name, the username and the default password provided will be needed for the connection of the datasource module to the Wildfly server.

### JDBC Driver Setup

Once the JDBC driver is downloaded, it has to be deployed in the WildFly server. After accessing the admin panel, navigate to the `Deployments` tab and add the driver by selecting the downloaded .jar file. 

### Datasource Configuration

From the WildFly admin panel, navigate to the `Configurations` tab and then, from the `Subsystems` menu element select `Datasources` and then `Non-XA`. Add a new datasource, select PostgreSQL from the list and in the first step fill in the datasource name (in this example its name is **PostgresDSUbi** and its JDNI is **java:/PostgresDSUbi**). 

In the second step, navigate to the `Detected Driver` tab and select the already deployed JDBC driver. If the element is not present in the list, consider logging out and then logging in again. 

Finally, provide the database URL (e.g. jdbc:postgresql://localhost:5432/database_name), the default username for the created database and the password already defined. 


**Note:**(Alternatively,  you can use the default datasource of WildFly that uses **H2:mem - java:jboss/datasources/ExampleDS** but beware that this type of datasource is volatile; it only stays in the memory and is never saved to the disk)


## Endpoints:

There's a Postman collection included in the project, nonetheless a brief description of each endpoint follows:

GET: http://localhost:8080/PhoneBook/ubi/phonebook/ Lists all contacts in JSON format

GET: http://localhost:8080/PhoneBook/ubi/phonebook/{id} Gets contact with the specific id in JSON format

POST: http://localhost:8080/PhoneBook/ubi/phonebook/ Accepts a new contact in JSON format (Example available in the Postman collection)

DELETE: http://localhost:8080/PhoneBook/ubi/phonebook/{id} Deletes contact with the specific `{id}`

UPDATE: http://localhost:8080/PhoneBook/ubi/phonebook/{id}

## Run the Project:

Make sure that WildFly is executing and the datasource is configured. Then, perform the deployment with the following command in the project folder: `mvn wildfly:deploy -DskipTests=True`.

## Undeploy the Project: 
Type:  `mvn wildfly:undeploy`
