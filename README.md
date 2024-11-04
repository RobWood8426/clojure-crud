# clojure-crud
A demo of a basic CRUD API built in clojure

In order to run this Demo you will need Java 21+ and the clojure CLI



curl -X GET http://localhost:3000/books

curl -X POST http://localhost:3000/books
    -H "Content-Type: application/json"      
    -d '{"title": "Clojure for the Brave and True", "published": "2019", "description": "A book on learning clojure"}'

curl -X PUT http://localhost:3000/books/1 
    -H "Content-Type: application/json" 
    -d '{"title": "Clojure for the Brave and True", "published": "2019", "description": "A book on learning clojure"}'

curl -X DELETE http://localhost:3000/books/1