# Scrumban
Scrumban is a web application for project management according to agile Scrumban methodology, which is a combination of Scrum and Kanban.

## Technologies
- Spring Boot 
- Spring Security
- JSON Web Token
- JPA
- PostgreSQL
- WebSocket
- Angular


## Features
- registering new accounts
- logging into an account (also by social media: Google and Facebook)
- creating, editing, and deleting projects
- sending invitations to participate in a project
- adding, editing, and deleting columns
- limiting work in progress for a specific column
- adding, editing, and deleting tasks
- assigning and dismissing user from a task
- posting comments
- preview of all assigned tasks to a user
- generating and exporting statistics for a project to CSV format
- editing profile and changing account settings
- changes made in a project will be immediately visible to all other users without the need to refresh the page (used WebSocket technology)

## Demo
The application is available here: https://scrumban1.herokuapp.com  
It's used the free deployment option on [Heroku](https://www.heroku.com), so if the application wasn't used for a longer time, rebuilding will be necessary after a new request was sent. This process takes about **30** seconds.

The database contains some generated data. You can use these initiated users to try and test the application:
|        Email         |  Password  |
| :------------------: | :--------: |
| hubigabi19@gmail.com | hubigabi19 |
| JohnSmith@gmail.com  | JohnSmith  |
| SamWright@gmail.com  | SamWright  |

![Demo 1](https://i.imgur.com/jmJbkXQ.png)
![Demo 2](https://i.imgur.com/yB1jcrv.png)
![Demo 3](https://i.imgur.com/ZN9f0jY.png)

## Usage
### To run the application you will need:
- Java 11
- Maven
- Node.js
- PostgreSQL

Firstly clone this repository and go to the project directory:
```shell
$ git clone https://github.com/hubigabi/scrumban.git
$ cd scrumban
```

### Backend

Change credentials to your database in **application.properties**

```shell
spring.datasource.url=jdbc:postgresql://localhost:5432/scrumban
spring.datasource.username=postgres
spring.datasource.password=root
```

Run the application using Maven:
```shell
$ mvn spring-boot:run
```
The application will be working on: http://localhost:8080  
REST API documentation will be available on: http://localhost:8080/swagger-ui.html

### Frontend
Install dependencies and run the application:
```shell
cd frontend/scrumban

# Install dependencies
$ npm install

# Run application
$ ng serve -o
```
The application will be working on: http://localhost:4200

### Docker
Run application with Docker:
```shell
$ git clone https://github.com/hubigabi/scrumban.git
$ cd scrumban

# Run application
$ docker-compose up
```
The application will be working on: http://localhost:8080 

## Created by
[Hubert Gabryszewski](https://github.com/hubigabi)