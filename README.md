# HIBERNATE EXAMPLE

There are 2 branches:
* **simple** focuses on most common hibernate usage patterns, places that can be improved are commented
* **improved** contains possible improvements over simple example

Start with:

1. [User entity](src/main/java/ru/hh/hibernateexample/users/User.java)
2. [UserDAO](src/main/java/ru/hh/hibernateexample/users/UserDAO.java)
3. [UserService](src/main/java/ru/hh/hibernateexample/users/UserService.java)
4. [UserServiceTest](src/test/java/ru/hh/hibernateexample/users/UserServiceTest.java)
5. [Main class](src/main/java/ru/hh/hibernateexample/Main.java)

To run Main class you need Postgresql installed.
After installation run [prepare-db-as-postgres.sh](src/main/sh/prepare-db-as-postgres.sh) to create tables and users.
