

# The Recruitment Application

This is the recruitment system application for IV1201 by group 10. 


## Tools 


* Version Control (Git)
* Project management (maven)
* Backend unit testing (Junit)
* Containerization (Docker)
* Continuous integration/development (Github Actions)
* Cloud runtime (Heroku)

## Frameworks
* React
* Spring boot 
	* Spring Security
	* Spring Validation
* Spring Data (JPA)
* JWT (json web token)

## Setup
below are the instructions on necessary setup that needs to be done for particular tasks:

### Environment variables 
#### Environment variables for Email
* `RECR_EMAIL` is the email address that is sending reset codes
* `RECR_EMAIL_PWORD` is the password of the email account above
* `RECR_EMAIL_HOST` the email server that hosts this reset service
* `RECR_EMAIL_PORT` port of the email server
#### Environment variables for running application locally
these variables need to be set in addition to those above to allow running the app locally.   

* `SPRING_PORT` the port that the back-end will run on
* `DATABASE_URL` is the url specifying necessary params for database access  `postgres://<username>:<password>@<host>/<dbname>`

### Postgres & Database 
Restore the database from `existing-database.sql` 

NOTE: remeber to give the necessary CRUD priviliges on this table to user specified in the `DATABASE_URL` which accesses this database
## How to Tasks

### How to run the application

1. Complete all the setup steps given in the setup section 
2. run the `start.bat` 
	this does the following
	1. changes directory to the "recruitment-backend" and runs command `mvn spring-boot:run`  to start the backend
	2.  changes directory to the "recruitment-frontend" and runs command `npm start`  to start the backend
3. 1. The application can now be reached at the url `http://localhost:3000` if running locally

### How to run with docker

TODO WRITE HERE







