# Hostfully - RESTful webservice

Pre conditions:
1. Install Java 17;
2. Install Maven 3.x;

# How to run:

1. Pull project;
2. Go to root directory and execute <b>mvn clean install</b> commpand;
3. Go to <b>hostfully-webservice\target</b> and execute <b>java -jar hostfully-webservice.jar</b>;

After successfull run application will create two log files in <b>hostfully-webservice\logs</b> directory:
1. hosftully-webservice.log;

# Access Swagger:
In order to access swagger open your favourite browser and enter following address: http://localhost:3000/swagger-ui/index.html

# Access H2 Database:
In order to access H2 database web console open your favourite browser and enter following address: http://localhost:3000/h2-console/
Edit following parameters in h2-login console:
1. JDBC Url: jdbc:h2:mem:hostfully
2. Username: hostfully
3. Password: hostfully
