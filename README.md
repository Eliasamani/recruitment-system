

# The Recruitment Application

This is the recruitment system application for IV1201 by group 10. 


## Tools 


* Version Control (Git)
* Backend build tools (Apache Maven)
* frontend build tools (Node.js)
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
### Required programs for the application
* Java 21 (JDK needed for development and building, JRE can be used on completed build)
* Node.js with npm
* Docker (Only for packaging builds)
### Environment variables 
#### Environment variables for Email
* `RECR_EMAIL` is the email address that is sending reset codes
* `RECR_EMAIL_PWORD` is the password of the email account above
* `RECR_EMAIL_HOST` the email server that hosts this reset service
* `RECR_EMAIL_PORT` port of the email server
* If you do not have an email server, set host to an empty domain or reserved domain such as example.com. Set port to an integer and set the email and password to any string. This will cause the sending function to print an error in the logs but the code will still be printed in the logs.
* The error in logs can be avoided by commenting out the call to resetService.sendMail in generateCode in ResetController
#### Environment variables for running application locally
these variables need to be set in addition to those above to allow running the app locally.   

* `SPRING_PORT` the port that the back-end will run on
* `DATABASE_URL` is the url specifying necessary params for database access  `postgres://<username>:<password>@<host>/<dbname>`

### Postgres & Database 
Restore the database from `existing-database.sql` 

NOTE: remeber to give the necessary CRUD priviliges on this table to user specified in the `DATABASE_URL` which accesses this database
## How to Tasks

### How to run the application during development

1. Complete all the setup steps given in the setup section 
2. Either use the start script or run commands manually
	#### Using start script:
     run the `start` script,
	this does the following: 
   	1. changes directory to the "recruitment-backend" and runs command `mvnw spring-boot:run`  to start the backend
    2.  changes directory to the "recruitment-frontend" and runs commands `npm install` and `npm start`  to start the frontend
 	#### Running commands manually:
	Run the following commands:
  	1. Open a terminal in the "recruitment-backend" directory and run `mvnw spring-boot:run` to start the backend
   	2. Open a second terminal in the "recruitment-frontend" directory and run `npm install`
   	3. Using the second terminal now run `npm start` to start the frontend
3. The application can now be reached at the url `http://localhost:3000` if running locally

### How to build and package the application

1. First build the frontend by navigating to the "recruitment-frontend" directory and running `npm run build`
2. Move all files from the created build folder into "recruitment-backend/src/main/resources/public" to static host them from the backend
3. Build the backend by navigating into the "recruitment-backend" directory and run `mvnw package` to build the backend to a jar file which is placed in recruitment-backend/target/. This jar also contains the static hosted frontend now. It can be run with only a JRE. 
4. (Optional docker packaging) Build the docker file which uses the file jar file in the recruitment-backend/target/ directory. Depending on the environment the docker image will be used in, the SPRING_PORT will have to be set to the value of another environment variable at runtime. It is currently being set as PORT, which is the environment variable used for the port in heroku, but this can be changed in the dockerfile.








