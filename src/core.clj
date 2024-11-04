(ns core
   (:require
    [ring.adapter.jetty :as jetty]
    [compojure.core :as compojure]
    [compojure.route :as route]
    [ring.middleware.defaults :as defaults]
    [db :as db]
    [clojure.data.json :as json]))

(compojure/defroutes routes
  (compojure/GET "/" [] "Welcome to the Clojure CRUD Demo")
  (compojure/GET "/books" [] (json/write-str (db/list-all-books)))
  (compojure/POST "/books" req 
    (let [book-data (json/read-str (slurp (:body req)) :key-fn keyword)] 
      (json/write-str
       (db/create-book book-data))))
  (compojure/GET "/books/:id" [id] (json/write-str (db/retreive-book (Integer/parseInt id))))
  (compojure/PUT "/books/:id" [id :as req] 
    (let [book-data (json/read-str (slurp (:body req)) :key-fn keyword)]
      (json/write-str
       (db/update-book (Integer/parseInt id) book-data))))
  (compojure/DELETE "/books/:id" [id] 
    (json/write-str
     (let [deleted-book (db/delete-book (Integer/parseInt id))]
       (cond
         deleted-book 
         {:status "Success"}
         
         :else 
         {:status "Failed - Book Not Found"}))))
  (route/not-found "Page Not Found"))

(def wrapped-handler
  (defaults/wrap-defaults routes defaults/api-defaults))

(defn -main
  "A simple entry point for our program."
  []
  (println "Loading Books into in memory DB")
  (db/load-books-into-db db/xtdb-node)
  (println "Starting Jetty server on 3000") 
  (jetty/run-jetty wrapped-handler {:port 3000 :join? false}))

(comment
  (def server
    (-main))
  
  (.stop server)
  ) 

  