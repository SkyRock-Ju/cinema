
<h3 align="center">Project JOB4J_Cinema</h3>

  <p align="center">
    Cinema project where user can administrate films, film sessions as admin and buy tickets as logged user

  </p>



<!-- ABOUT THE PROJECT -->
## About The Project


Project is a simple cinema thymeleaf website where you can watch film sessions, films and edit them. Project uses postgres sql for database
There are 3 types of access:

Guest:
* Only read access. Just watch film sessions and films

Logged in User
* They have links in film sessions that leads to buying ticket page

Admin
* Admins have 2 additional links on the header to add film and to add film session
* They can edit already existing films and sessions

## Technology stack.

- **Java 17**
- **Spring Boot 3.0.5**
- **HTML 5**
- **Thymeleaf 3.0.5**
- **BOOTSTRAP 4.4.1**
- **Liquibase 4.15.0**
- **sql2o 1.6.0**
- **H2 2.1.214**
- **Mockito 3.5.13**
- **Junit 5**

## Environment requirements.

- **Java 17**
- **Maven 3.8**
- **PostgresSQL 14**
## Initializing project.

- **Create DB**

``` shell 
create database cinema;
```

- **Run the project**

``` shell 
mvn spring-boot:run
```

## Interactions with application.

-On main page you can see information about resources.
![](images/1.PNG)

-On film sessions page there are information about hall, movie name, start time, end time, ticket price.
![](images/2.PNG)

-As ADMIN ACCESS on this page you can click the link by the movie name and edit chosen film session.
![](images/3.PNG)

-On movies page there are information about movie name, description, release year, age restriction, duration of the movie and genre.
![](images/4.PNG)

-As ADMIN ACCESS on this page you can click the link by the movie name and edit chosen movie.
![](images/5.PNG)

-Only registered users can buy tickets. The registration page.
![](images/6.PNG)

-Login page.
![](images/7.PNG)

-If user input has errors, there are invalid form page for that.
![](images/8.PNG)

-As logged-in user there are feature to buy ticket by clicking the link on film sessions page.
![](images/9.PNG)

-If purchase was success user should see notification.
![](images/10.PNG)

-If ticket is unavailable user will see an error.
![](images/11.PNG)

## Контакты.
- skyrockpk@gmail.com
- <a href="https://t.me/julus_skyrock" target="_blank">Телеграм</a></h1>

