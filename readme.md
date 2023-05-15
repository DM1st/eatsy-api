# Eatsy API

A spring boot REST API that user-facing services can interface with for finding, creating and sharing your favourite
recipes.

[![CircleCI build](https://circleci.com/gh/DM1st/eatsy-api/tree/develop.svg?style=shield)](https://circleci.com/gh/DM1st/eatsy-api/tree/develop)
[![Coverage Status](https://coveralls.io/repos/github/DM1st/eatsy-api/badge.svg?branch=develop)](https://coveralls.io/github/DM1st/eatsy-api?branch=develop)
[![Create and publish a Docker image](https://github.com/DM1st/eatsy-api/actions/workflows/publish.yml/badge.svg)](https://github.com/DM1st/eatsy-api/actions/workflows/publish.yml)

* A live demo version of the Eatsy API is deployed and the Swagger UI Spec for the service can be
  found [here](https://eatsy-api.onrender.com/swagger-ui/index.html#/). Please note, the application uses free-tier
  services, so it will take a few minutes for the API service to spin up.
* To try out the Eatsy API via a UI, you can use the [eatsy-react-ui](https://github.com/DM1st/eatsy-react-ui/) which is
  a user-friendly React application that allows users to interact with the Eatsy API. A live demo version is deployed
  and integrated with the Eatsy API [here](https://eatsy.onrender.com).

Together, these components (along with a Postgres database) make up the end-to-end Eatsy solution and the live demo
instances are hosted on the [Render](https://render.com) platform.

## Getting started

The live demo version of the service is deployed using the dockerfile in the project root. However, you can deploy your
own instance locally:

### Build and deploy the spring boot service locally

The server is run as a [Sprint Boot](https://spring.io/projects/spring-boot) application. For simplicity reasons it
uses [The Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) (eliminating the need to
install Gradle locally). This helps ensure version consistency, easy setup, build reproducibility, and improved build
performance.

#### Pre-requisites

* JDK (this project has been developed using JDK 20) installed and configured on your local machine to be able to complie and run the Java code.
* Before running the service, you will need to have a Postgres instance installed and need to update the Spring Boot
  ```application.properties``` file to configure the connection details (e.g. host, port, username and password) to
  align to your database instance that the application will use to connect to it. For example:

```
##local dev PostgreSQL datasource properties
spring.datasource.url=jdbc:postgresql://localhost:5432/eatsy
spring.datasource.username=<your postgres username>
spring.datasource.password=<your postgres password>
spring.jpa.show-sql=true
```

```application.properties``` can be found at:

```<projectRoot>\eatsyAppService\eatsyAppService-controller\src\main\resources\application.properties```

If you don't want to install postgres on your machine, you can use a H2 in memory database by using thr following command:

```
./gradlew clean build bootrun --args='--spring.profiles.active=h2'
```

#### Running the service can then be done from a bash terminal:

```
./gradlew clean build bootrun
```

Once the server is running navigate to http://localhost:8080/swagger-ui.html to see the Swagger UI Spec.

#### Please note:

If initiating through an IDE such as intelliJ (rather than command line), you will need to ensure the "Working
Directory"
is correctly set (to the same as the Spring Boot application runner) otherwise you will get initialisation errors when
trying to locate logging config and other properties. The working directory in your IDE run configuration should be set
to:

```C:\<location to repository root>\eatsyAppService\eatsyAppService-controller```

#### Populating your local instance with random recipe test data

Random recipe generation can be triggered externally and persisted for manual testing and verification purposes. With
the server running, open a new bash terminal (at the project root) and run the following command:

```
./gradlew generateRandomRecipes
```

This executes a custom gradle task that generates a random number of recipes (between 1 and 15) which then get posted to
your local application's `addRecipe` endpoint. This can be executed as many times as needed to reach the level of test
data required.

If there is a need to remove all data in the database, the following commands can be used to drop all the tables:

```
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
```

This works because all the tables are in a single schema named public. If you are using PostgreSQL 9.3 or later, you may
also need to restore the default grants.

```
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;
```

## Project Structure and configuration

This application uses Gradle as the build tool and is structured as Gradle sub-projects. This helps separate the
different responsibilities and concerns of the application into distinct modules. This contributes to keeping the
codebase organized and maintainable, and makes it easier to test, debug and deploy the application.

The structure of the Gradle projects looks like this, with each sub-project focussing on a specific aspect of the
application:

```sh
eatsyAppService          # Umbrella Gradle project
|
|
+-- controller           # Contains the controllers of the application. 
|                        # Responsible for handling HTTP requests and 
|                        # returning the appropriate response. 
|                        # They interact with the services of the application 
|                        # to retrieve data.
|
|
+-- domain               # Contains the domain objects of the application. 
|                        # These represent the entities and data that the 
|                        # application manipulates. 
|                        # Some business logic is also contained here.
|
|
|
+-- model                # Contains the data transfer objects (DTOs) of the application. 
|                        # In this case, they are used to map the domain objects 
|                        # to a representation that can be sent via HTTP requests/responses
|                        # to other independent components that interact with this API.
|                                        
|
|
+-- model-mappers        # Contains the mappers of the application. 
|                        # The Mappers are responsible for mapping between the DTOs, 
|                        # Domain objects and Entity objects (that get persisted to 
|                        # the database) and they provide a way to convert between
|                        # these representations as needed.
|
|
+-- persistence          # Contains the persistence layer of the application. 
|                        # Responsible for storing and retrieving data (via the Entity 
|                         # objects), and interacting with the postgres database.
|
|
|
|
+-- service              # Contains the services of the application. 
|                        # These are responsible for implementing the business logic 
|                        # of the application.
|
|
|
|
+-- test-data-generation # Generates random test data that is used by the unit tests 
|                        # in all the above sub-projects. This sub project helps  
|                        # improve the reliability of the unit tests. 
|                        # By generating random test data, the tests will test edge cases that 
|                        # may not have been considered with hard-coded test data. 
|                        # This helps catch unexpected behavior and ensures that 
|                        # the code works as intended.
```

- Each sub-project has its own build configuration dependencies (in the respective ```build.gradle``` files) and tests
  cases.
- Changes in one sub-project won't affect the others.
- Improved build performance: This Gradle sub-projects structure allows for building only the parts of the application
  that have changed. This can result in faster build times.
- Better modularity: This Gradle sub-projects structure allows you to develop and deploy your application as modular
  components, which makes it easier to reuse code across different projects and create smaller, more focused artifacts.

### Logging

This project uses log4J2 with the logging config (```application.properties``` and ```log4j2.properties```) located in:

```eatsyAppService\eatsyAppService-controller\src\main\resources```

Logs for all Gradle sub-projects write to ```logfile.log``` located in the ```eatsyAppService-controller```  project:

```\eatsyAppService\eatsyAppService-controller\logs```

Logging levels for the logfile can be changed in log4j2.properties file.

### Testing

This project uses [JUnit 5](https://junit.org/junit5/) as the unit test framework. In addition, the below libraries and
frameworks are also used for testing:

#### Faker

[Faker](https://github.com/DiUS/java-faker) has been used for generating random, localised test data in a variety of
formats. The data generated is used in unit tests to validate the behavior of the application. By generating random test
data, the tests will test edge cases that may not have been considered with hard-coded test data. This helps catch
unexpected behavior and ensures that the code works as intended.

As mentioned in the getting started section, random recipe generation can also be triggered externally and persisted for
manual testing and verification purposes.

With the server running, open a new bash terminal (at the project root) and run the following command:

```
./gradlew generateRandomRecipes
```

This executes a custom gradle task that generates a random number of recipes (between 1 and 15) which then get posted to
your local application's `addRecipe` endpoint. This can be executed as many times as needed to reach the level of test
data required

#### Mockito

[Mockito](https://site.mockito.org/) is a mocking framework for Java that has been used in the unit tests to isolate the
code being tested from its dependencies. By using mock objects in the unit tests, the project avoids having to set up a
full test environment, which speeds up test execution and makes the tests more focussed on the unit of code under test.

#### Coveralls

[Coveralls](https://coveralls.io/) is a web-based service that provides this project with test coverage analysis (as
illustrated in the coverage badge at the top of this readme). This allows for codebase test coverage to be analysed and
identify areas where more tests are needed. This has been used to help ensure that the code is thoroughly tested and to
reduce the risk of bugs in production.

The coveralls task can be executed with the following command at the project root:

``` ./gradlew coveralls ```

The html report can be found at ```project root > build > reports > jacoco > jacocoRootReport > html > index.html```

### API Documentation

A JSON Swagger spec can be found at '/v3/api-docs'. Locally this will be 'http://localhost:8080/v3/api-docs'.

A UI version can be seen locally using the [Swagger UI page](http://localhost:8080/swagger-ui.html).

This Swagger specification is a file that specifies the structure of the API. It is a user-friendly interface that
defines the endpoints, requests and response formats and the data models that are used by the API. This makes it easier
to generate client-side code for consuming the API (such as
the [eatsy-react-ui](https://github.com/DM1st/eatsy-react-ui/)) since the specification provides all necessary
information about the API's structure, therefore reducing the risk of defects.

The Swagger Spec is created with the ```springdoc-openapi-ui``` library and configured in the ```openApiConfiguration```
file located in the ```eatsyAppService-controller``` project.

#### Manual inspection of a local API deployment

As mentioned above, this service has unit tests (using [JUnit 5](https://junit.org/junit5/)
, [Mockito](https://site.mockito.org/)
and [Faker](https://github.com/DiUS/java-faker)) with test coverage displayed on the coverage badge in this ReadMe.
However, this Swagger UI provides a way for the API to be tested manually by making requests and seeing the response
directly in the browser. This allows for easier debugging and validation of the API's behaviour. Example below:

* Once the service is running navigate to the [Swagger UI page](http://localhost:8080/swagger-ui.html).
* Select the 'Api Controller'
* Select the 'POST' request for adding a new recipe.
* Select "Try it out" and enter a request body of your choice as a test. E.g.

```
{
  "ingredientSet": [
    "Beans", "Toast"
  ],
  "method": {
    "1": "Mircowave Beans",
    "2": "Toast Bread",
    "3": "Combine Beans and Toast"
  },
  "name": "Beans on Toast"
}
```

* Select "Execute".
* Inspect the server response and the response body will display the new recipe object that has been created with your
  chosen name.

### Deploy your own instance (via Docker & Docker Compose):

**Pre-requisite:** Docker & Docker Compose installed on your machine if you wish to build and run the Docker images in
the repository. The method offers an alternative approach by allowing Postgres, PgAdmin and the Eatsy-API to be deployed
and run as Docker containers.

The built Docker image of the Eatsy API can be found on
DockerHub [here](https://hub.docker.com/r/dm1st/eatsy-api-docker)

- Copy the docker-compose.yml from the repository located at:

```<projectRoot>\alternativeDeploymentOptions\docker-compose.yml```

*_Please note this is not the docker-compose.yml file located at project root (which builds the image from the
dockerfile rather than this config which pulls the latest built image from dockerhub).

- Transfer the alternativeDeploymentOptions\docker-compose.yml file to your machine where you will be running the
  service. (Your machine will need Docker and Docker-Compose installed as detailed in the pre-requisite)
- Run the following command in the directory where you have placed the docker-compose.yml file to run the application:

```
docker-compose up -d
```

This command will run the eatsy API service, the Postgres DB and pgAdmin in Docker Containers.

For future deployments, explicitly check the dockerhub repository to ensure you are building the image with the latest
changes:

```
docker pull dm1st/eatsy-api-docker && docker-compose up -d
```

- The service can be spun down with the ```docker-compose down'``` command.

With Docker-Compose the Postgres container is started and established first as the API service depends on the
postgresSQL container being up and healthy.

- Navigate to the Swagger UI page to see the API service running

```http://<your_host>:8080/swagger-ui.html```

* You can also follow the above steps, but with the ```docker-compose.yml``` file located at the project root to build
  an image from the project dockerfile, rather than pulling the built image hosted on dockerhub. If using this approach,
  you will need the codebase stored locally on your host (rather than just the docker-compose.yml file located in the
  alternativeDeploymentOptions folder).

### The live instance of this application on Render.com

As mentioned above, the Eatsy API is deployed on Render by using the ```dockerfile``` in the project root. As this
project uses the free-tier products on [Render.com](https://render.com), therefore only the most lightweight
infrastructure is available. As a result, in order to make deployment possible, and reduce image spin-up times (when the
service is cold)
every effort has been made to reduce the docker image size. This includes:

* Layering the Spring boot Jar file so that it is built into 4 layers. This is done in the Controller
  project ```build.gradle``` file.
* The decomposition of the application into different layers allows for the creation of an efficient Docker image, by
  creating a multi-stage ```dockerfile``` (located in the project root). Efficiency is improved and start up time of the
  docker image reduced, especially as it is being run on limited infrastructure.

## TODO

* Add Google shopping list integration.
* Add ability to search for recipe.
* Add search capability for best match based on search ingredients.
* Add the concept of users.
* Add component test pack.
