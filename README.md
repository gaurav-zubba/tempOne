# leanix-fpe

## Compile, build and install

To clean the existing target folder run the following command:
```
mvn clean
```

To install the required dependencies, create necessary JAR files and run the tests, run the following command:
```
mvn package
```

## Run the application

To run the application on the specified port in the configuration.yml file, run the JAR file generated from above with the following command:
```
java -jar target/LeanIXFPE-1.0-SNAPSHOT.jar configuration.yml
```

## Running tests

To run the tests, use the following command:
```
mvn test
```
