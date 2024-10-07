# XP-Neo4j
This project is for experimenting with neo4j graph database.

## Data Creation
Under resources/data-creation there are `.http` scripts that you can run, after launching the application and its docker-compose, file that will populate the db with data.
You have to make sure as well to select an environment for the scripts to read some variables.

## Persistence Implementation
The persistence layer is implemented using various ways:
- Neo4jRepository
- Neo4jTemplate
- Neo4jClient

To pick one of them, you have to set the value of the `myapp.persistence.strategy` property in the `application.properties` file.