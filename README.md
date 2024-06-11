### Running Application

#### Prerequisites
- Make sure you have Docker and Docker Compose installed on your machine.
- Add your `.env` file with credentials like the example:
  ```
  SPRING_MAIL_HOST=<smtp host>
  SPRING_MAIL_USERNAME=<email address>
  SPRING_MAIL_PASSWORD=<password>
  ```
#### Usage
To run the application, follow these steps:
1. Build Docker Compose:
````
docker-compose build
````
2. Run Docker Compose:
````
docker-compose up
````
3. Once the containers are up and running, you can access the Spring Boot application at http://localhost:9097.

Also, you can change variables in env file.

#### Shutting Down
To stop the containers and remove the Docker network, run:
````
docker-compose down
````
