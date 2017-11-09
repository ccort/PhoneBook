# PhoneBook

## Software used:

**[JDK 8](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)**

**[WildFly 11](http://wildfly.org/downloads/)** as the Application Server 

**[Apache Maven 3.5.2](https://maven.apache.org/download.cgi)** 

**[JDBC Connector 9.4.1212 JDBC 42](https://jdbc.postgresql.org/download.html)**

**[PostgreSQL 10](https://www.postgresql.org/download/)** or another DBMS of your choice

**[IntelliJ Ultimate 2017](https://www.jetbrains.com/idea/download/)**  *(Optional)*  for development but the project will run fine just with Maven

**[Postman](https://www.getpostman.com/)** for endpoint testing

## Instructions:

### WildFly Setup
Start the WildFly server by navigating to the WildFly \bin directory and executing the `.\standalone.bat` script.

A management user account has to be created for WildFly. Just run the `.\add-user.bat` script to create one.

The WildFly admin panel is accessed by the following link:  http://localhost:9990/console/

### Database Creation

Use the PostgreSQL admin application, connect to the default database with the credentials defined during the installation and create a new database. Its name, the username and the default password provided will be needed for the connection of the datasource module to the Wildfly server.

### JDBC Driver Setup

Once the JDBC driver is downloaded, it has to be deployed in the WildFly server. After accessing the admin panel, navigate to the `Deployments` tab and add the driver by selecting the downloaded .jar file. 

### Datasource Configuration

From the WildFly admin panel, navigate to the `Configurations` tab and then, from the `Subsystems` menu element select `Datasources` and then `Non-XA`. Add a new datasource, select PostgreSQL from the list and in the first step fill in the datasource name (in this example its name is **PostgresDSUbi** and its JDNI is **java:/PostgresDSUbi**). 

In the second step, navigate to the `Detected Driver` tab and select the already deployed JDBC driver. If the element is not present in the list, consider logging out and then logging in again. 

Finally, provide the database URL (e.g. jdbc:postgresql://localhost:5432/database_name), the default username for the created database and the password already defined. 


**Note:**(Alternatively,  you can use the default datasource of WildFly that uses **H2:mem - java:jboss/datasources/ExampleDS** by editing the `<jta-data-source>` tag in resources/META-INF/persistence.xml but beware that this type of datasource is volatile; it only stays in the memory and is never saved to the disk)


## Endpoints:

There is a Postman collection included in the project; nonetheless a brief description of each endpoint follows:

**GET:** `http://localhost:8080/PhoneBook/ubi/phonebook/` Gets all contacts in JSON format and returns a 200 OK

**GET:** `http://localhost:8080/PhoneBook/ubi/phonebook/{id}` Gets contact with the specific `{id}` in JSON format, returns 200 OK on success or a 404 if no contact with the the specific `{id}` is found

**POST:** `http://localhost:8080/PhoneBook/ubi/phonebook/` Accepts a new contact in JSON format and returns 200 OK with a message containing the URI of the newly created contact. If no body content is sent it returns a Bad Request.

**DELETE:** `http://localhost:8080/PhoneBook/ubi/phonebook/{id}` Deletes the contact with the specific `{id}`, returns 204 on success, 404 if contact not found

**UPDATE:** `http://localhost:8080/PhoneBook/ubi/phonebook/{id}` Updates the contact with the specified `{id}`. The updated components of the contact should be sent in JSON format. If fields are omitted the old values remain unchanged. Returns 204 on success or 404 on contact not found

## Run the Project:

Make sure that WildFly is running and that the datasource is configured. Then, perform the deployment with the following command in the project folder: `mvn wildfly:deploy` (you can also add the `-DskipTests=True` argument to skip the tests).

## Undeploy the Project: 
Type:  `mvn wildfly:undeploy`
