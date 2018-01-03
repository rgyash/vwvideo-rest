Assumptions and consideration 
1)	Basic Authorization was used to illustrate customer registration and video service.  For production application OAuth2, OpenID Connect, multifactor app can be considered for authentication and authorization
2)	In memory H2 database was used to store user information.  For production applications other relational database like MySQL, Oracle etc and NOSQL database like Cassandra, MongoDB can be used
3)	Apache Tika was used for basic validation for MP4.  Other solutions can be considered.
4)	Spring Cloud solution including Config Server, Hystrix, Zuul (now Spring Gateway), sleuth etc. will be used for complete micro services.  In this scenario User Registration and Video service will be separate micro services using DDD and bounded context
5)	Open source Spring content store was used to store mp4 files which follows Spring Data JPA patterns.  Using this solution saving the mp4 file in S3 is just a matter of configuration and adding one annotation to few classes
6)	Solution can be further optimized for scalability and fault tolerance.
7)	Solution was constructed to demonstrate understand and capability and time factor was considered in writing the code
8)	Java 1.8 
9)	Uses Spring booth with default Tomcat server.  Can be changed to other servers.
10)	Service store the mp4 file in filesystemRoot: c:\temp.  Please modify application.yml file for alternate location
11)	Swagger was integration for API documentation
12)	Project Lombok can be easily interated
13)	Spring Cloud AWS module can be used for AWS
14)	Validation for upload file time, content type were done server. This can also be done on client for a faster response and less load on server

Compiling and Running
1)	Import the project in any IDE like Eclipse, Spring STS or IntelliJ.  Ensure Maven plugin, Java 1.8 is available
2)	Mvn clean install
This will build and run all the test case
3)	Run the Spring boot application
