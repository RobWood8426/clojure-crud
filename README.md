# Clojure Crud
A demo of a basic CRUD API built in clojure

In order to run this Demo you will need Java 21+ and the [clojure CLI](https://clojure.org/guides/install_clojure)


To start up the API run the following:

```
clj -M:xtdb:server
```


Here are some commands to get you started with testing the API

```
curl -X GET http://localhost:3000/books

curl -X POST http://localhost:3000/books
    -H "Content-Type: application/json"      
    -d '{"title": "Clojure for the Brave and True", "published": "2019", "description": "A book on learning clojure"}'

curl -X PUT http://localhost:3000/books/1 
    -H "Content-Type: application/json" 
    -d '{"title": "Clojure for the Brave and True", "published": "2019", "description": "A book on learning clojure"}'

curl -X DELETE http://localhost:3000/books/1
```

##Discussion

[XTDB](https://www.xtdb.com/) Is a natural choice as it provides a basic EAV store with bitemporality, and can use any database engine. In this case an in memory DB is used and it is bootstrapped on server startup
Clojure with is functional style and immutible datastructures is a great fit for restful APIs, compojure allows declaritive routes and simple/reasonable routing

The API itself is simple. Doing no more than is required in order to be be performant and correct. 
Ring allows for simple request semantics and provides sensible handler middleware defaults. 

Jetty is about as battle tested as web-servers come and is well supported by any integrations we may need down the line. Particularly when it comes to observabillity, which becomes necessary as a project grows

### Next Priorities

The natural next priorities in this project would be to appropriately cater for edge cases
Include API documentation through something like swagger
Implement HATEOAS in order to allow for clientside discoverabillity 
Implement layers of abstraction above the web-server (Reverse-proxy ssl-signing caching load-balancing) based on environment
