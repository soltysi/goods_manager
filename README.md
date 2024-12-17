### This project is part of the Shopify platform for handling products. It includes a database and a web application built with Spring Boot.

## How to Run the Project

### 1. Clone the Repository

Start by cloning the repository to your local machine:

```bash
git clone <your repository URL>
cd <project directory name>
```
###  2.Run Maven Commands
Before spinning up the container, you need to build the project using Maven:
```bash
mvn clean install
```
This command will build the project and prepare all necessary dependencies for running the application.
### 3.Start the Docker Container
Once the project is built, the next step is to start the database and web application using Docker:
```bash
docker-compose up --build
```
This command will:

- Build and start the Docker containers.
- Start the PostgreSQL database.
- Launch the web application.

### 4.Check the Application
After Docker finishes setting up the containers, you can visit your application at 
```http://localhost:8080``` (or the appropriate URL) and check if everything is running correctly.

Notes
 - All data regarding users and products are stored in the database.
 - Make sure the database is running before attempting to interact with the API.