# SpringBootAuthJWTStarter
Starter project for Your Spring Boot Apps with JWT authentication


## About
- Spring Boot Security Starter Project with JWT Authentication and Authorization
	- User registration
	- User login using their email and password
- Java 17
- Spring Boot 3.3.2


##  Usage 🗺️
1.  Fork the repository
2. Clone the repository
3. Using the pgAdmin interface or your psql, create a database called jwtstarter. If you wish to name this database different, update the database configuration in the application.properties file.
4. Update application.properties file with your database username and password
![image](https://github.com/user-attachments/assets/5015eb91-ffdc-45cc-9afa-f29690014928)
5. CREATE 'USER' in ROLE table  ![image](https://github.com/user-attachments/assets/337c1348-369a-4582-aacf-2b49c45bd617)

6. Run Project




##  Endpoints 🚀
- ***POST localhost:8080/api/v1/auth/register***  
request: 
```json
{
     "username":"john123",
     "password":"JohnJohn123",
     "passwordRepeated":"JohnJohn123",
     "email":"john123@gmail.com",
     "firstName":"John",
     "lastName":"Smith"
}
```
- ***POST localhost:8080/api/v1/auth/login***

request: 
```json
{
    "email":"john123@gmail.com",
    "password":"JohnJohn123"
}
```
response:
```json
{
     "accessToken": "eyJh...tkg", 
    "tokenType": "Bearer ",
    "user": {
        //user object
		...
		}
}
```

- ***GET localhost:8080/api/v1/auth/username/{username}***  
( Check username availability ) 

- ***GET localhost:8080/api/v1/auth/email/{email}***  
( Check email availability ) 
----
To change protected routes edit SecurityConfig file:
![image](https://github.com/user-attachments/assets/93f6788d-8db0-4f2d-861b-638554c4ecde)

Attach **Authorization** Header to requests that need authorization:
`Bearer eyJh...vwlr8Q`

-----



***If you found this helpful, please give it a star ⭐!***

