# Help-API
Welcome to the Help-API project. This is a RESTful Web API designed to facilitate the connection between clients and service providers. Whether you need a plumber for a specific time period or you're a professional looking to offer your services, Help-API serves as a bridge to bring you together.

The API has been developed using Java and Spring Boot, chosen for their robustness and the efficiency they bring to web development. For data storage, we utilize a PostgreSQL database, offering powerful, open source object-relational database system with a strong reputation for feature robustness, extensibility, and performance.

This API forms the backend part of the project, responsible for handling and processing requests. Although there's no frontend component directly attached, you can interact with it using API tools such as Postman or cURL, or you can use it as the backend for your own frontend project.

## Prerequisites
Here's a list of tools you need to install in order to run this application:
* [Java 17](https://www.oracle.com/java/technologies/downloads/)
* An IDE (ex. [Intellij IDEA](https://www.jetbrains.com/idea/download/))
* [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
* [PostgreSQL](https://www.postgresql.org/download/)
* You have basic understanding of RESTful APIs, Java, Spring Boot and SQL databases.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Installing help-api
1. Clone the repository
```
git clone https://github.com/mericakgul/help-api.git
```

2. Open the project
   Open the cloned project in preferred IDE.


3. The project uses Maven for dependency management. All required dependencies are listed in the pom.xml file and should be automatically handled by your IDE. If for some reason they aren't, manually import the pom.xml file in your IDE.


4. You need to set up your PostgreSQL database according to the application properties file (src/main/resources/application.properties). Update this file with your PostgreSQL username, password and database url.


### Using help-api
1. Once you've set up your database and imported the Maven dependencies, you can start the application by any of the following ways:
* Run HelpApiApplication.java

or
* Run the following command in the directory of the project in the terminal. 
   You do not need to install maven to your computer since the project has mvn wrapper so just run the following command but in the direction of where pom.xml is.
```
./mvnw spring-boot:run
```
or
* You can use the IntelliJ configuration on right top corner to run HelpApiApplication class.

or
* You can open the Maven tab on the right side of IntelliJ, click on 'Execute Maven Goal' button and click on 'mvn spring-boot:run' option.


The application will run at `localhost:8080` by default. You can change this in the application.properties file if needed.


2. You can then access various API endpoints via tools like Postman or Curl. You can find the Postman collection file under 'resources' folder. You can use it to test the API endpoints.

3. You can also access the API documentation by visiting the following URL in your browser:
```
http://localhost:8080/swagger-ui.html
```
This will open the Swagger UI interface, where you can explore the available endpoints, make API requests, and view responses.

### Help
Any recommendation is welcome. Please contact me if you have any idea, improvement or suggestion.

### Authors

Meriç Akgül

meric.ext@gmail.com
