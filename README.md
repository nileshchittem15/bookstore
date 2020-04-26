# Bookstore

The online store provides the following functionality
1. Add a Book to the store
2. Update the stock quantity of a book in inventory
3. Search book based on ISBN/Title/Author
4. Search media coverage about a book, given its ISBN
5. Buy a book

# Service Documentation

https://docs.google.com/document/d/1BnIwaexe5ToqxHHq-f19EuA4RROKUy4047FjJt7fxNY/edit?usp=sharing

# Local Build

Requirements : JDK 8, Maven
Command: mvn clean compile spring-boot:run

The application will start running on 8080 port

# Docker Build

docker pull nileshreddy/bookstore:3
docker run -p 8080:80 bookstore

The application will start running on 8080 port

# Postman Api Collection

https://www.getpostman.com/collections/964b1762a4b67cc46261
