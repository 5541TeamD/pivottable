# Pivot Table
This is a project for our Software Engineering class.

###Getting started
You will need Java (jdk 8) and nodejs to build the project.

Clone the repository:
```
git clone https://github.com/5541TeamD/pivottable.git
```
####Build the UI project
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

####Launch the application

I created a Gradle task called `launchServer`

To run it, go to the root of the project and run the gradle wrapper command
```
./gradlew launchServer
```
In windows, omitting the `./` might be required (or using .\\ instead in
in the case of powershell).  
This will build the java project and run the `Application.main` function.
You can ctrl-c to stop the process.

####Database Connection

A connection to an online or offline MySQL database is required for using the application. To enable this connection, MySQL JDBC connector JAR file should be placed in the classpath.
For logging into the database, its URL, username and password (optional for some databases) must be provided. Once connected, all the tables stored in the database will be listed and can be used to generate pivot tables.
