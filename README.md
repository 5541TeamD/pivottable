# Pivot Table
This is a project for our Software Engineering class.

### Getting started
You will need Java (jdk 8) and nodejs to build the project.

Clone the repository:
```
git clone https://github.com/5541TeamD/pivottable.git
```
#### Build the UI project
Go to the ui project directory:  
From the project folder
```
cd src/main/resourcers/ui
```
(Note: use backslash '\\' for windows or powershell command prompt instead of forward slash '/') 

Install UI dependencies:
```
npm install
```
After it has finished installing the dependencies, you can proceed to build the UI
```
npm run build
```
This will create a build folder under `src/main/resources/ui`

#### Set up the database

A MySQL database has to be setup to support saving users and pivot table schemas.
The script `src/main/java/ca/concordia/pivottable/scripts/Application_User_DB_Scripts.txt` will create the necessary tables. Simply login to the database as the desired user, create a database and launch the script.
Assuming you launch `mysql` from `src/main/java/ca/concordia/pivottable/scripts/`  
```
CREATE DATABASE app_user_db;
USE app_user_db;
source Application_User_DB_Scripts.txt;
```

#### Configuration

There is a configuration JSON file that can be used to change some default configuration. An example `configuration.json.example` file is provided.
By default, the application searches for a file called `configuration.json` in the working directory and looks for these values: 
 * `appServerPort`: Application port number (default: `4567`)
 * `appDatabaseUrl`: User database url (default: `jdbc:mysql://localhost:3306/app_user_db`)
 * `appDatabaseUser`: User database username (default: `root`)
 * `appDatabasePassword`: User database password (default: `root`)
The default values shown above are taken if the file is not found.

Alternatively, the user can specify the full path of the configuration file by adding a property `app.server.config.location`.
Example using arguments to set the System property:  
```
java -jar pivottable-all-2.0-SNAPSHOT.jar -Dapp.server.config.location="/home/user/configuration/pivottable_config.json"
```

#### Launch the application

I created a Gradle task called `launchServer`

To run it, go to the root of the project and run the gradle wrapper command
```
./gradlew launchServer
```
In windows, omitting the `./` might be required (or using .\\ instead in
in the case of powershell).  
This will build the java project and run the `Application.main` function.
You can ctrl-c to stop the process.

#### External Database Connection

A connection to an online or offline database is required for using the application. 
Currently, MySQL and PostgreSQL are supported.  
MySQL and PostgreSQL connectors will be included in the classpath by the gradle script. 
For logging into the database, its URL, username and password (optional for some databases) must
be provided by the user in the user interface to generate a pivot table. Once connected,
all the tables stored in the database will be listed and can be used to generate pivot tables.

#### Build a jar file

Once the UI is built, it is possible to package all dependencies (including the mysql and postgresql jdbc connectors)
in a single jar file. There is a gradle task `fatJar` that will manage that.
```
./gradlew fatJar
```
It produces a file `pivottable-all-2.0-SNAPSHOT.jar` in the `build/libs` directory.
Assuming `java` is in the path, the following command will run the server:
```
java -jar pivottable-all-2.0-SNAPSHOT.jar
```

#### Known issues

Known issues can be found in [the Github repository](https://github.com/5541TeamD/pivottable/issues)
along with the source code.



