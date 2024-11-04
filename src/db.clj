(ns db
  (:require
   [xtdb.api :as xt]
   [clojure.java.io :as io]
   [clojure.edn :as edn]))

(defonce xtdb-node (xt/start-node {}))

;; Insert a document into XTDB
(defn add-document
  "Add a document to XTDB."
  [node id attrs]
  (let [doc (merge {:xt/id id} attrs)]
    (println attrs)
    (xt/submit-tx node [[::xt/put doc]])
    (xt/sync node)
    doc))

(defn delete-document [node doc-id]
  (xt/submit-tx node [[::xt/delete doc-id]]))

;; Fetch a document from XTDB
(defn fetch-document
  "Fetch a document from XTDB by ID."
  [id]
  (xt/entity (xt/db xtdb-node) id))

(defn book-data []
  (edn/read-string
   (slurp
    (io/resource "books.edn"))))

(comment
  book-data)

(defn load-books-into-db [node]
  (doall
   (map-indexed
    (fn [idx book]
      (add-document node idx (assoc book :entity/type :book)))
    (book-data))))

(defn get-max-id [node]
  (let [db (xt/db node)
        query
        '{:find  [?id]
          :where [[e :xt/id ?id]]
          :order-by [[?id :desc]]
          :limit 1}]
    (ffirst (xt/q db query))))

(defn list-all-books []
  (map
   (fn [[title published description id]]
     {:id id
      :title title
      :published published
      :description description})

   (let [query
         '{:find [title published description id]
           :where [[b :entity/type name?]
                   [b :book/title title]
                   [b :book/published published]
                   [b :book/description description]
                   [b :xt/id id]]
           :in [name?]}]
     (xt/q
      (xt/db xtdb-node)
      query
      :book))))

(defn create-book [{:keys [title published description] :as input-book}]
  (let [saved-book
        (add-document
         xtdb-node (inc (get-max-id xtdb-node))
         #:book{:title title
                :published published
                :description description
                :entity/type :book})]
    
    (merge input-book {:id (:xt/id saved-book)}))
  )

(defn retreive-book [id]
  (let [{:book/keys [title description published] :keys [xt/id]}
        (fetch-document id)]
    {:id id
     :title title
     :published published
     :description description}))

(defn update-book [id attrs]
  (add-document xtdb-node id attrs))

(defn delete-book [id] 
  (let [entity (xt/entity (xt/db xtdb-node) id)]
      (delete-document xtdb-node id) 
      entity))

(comment

  (delete-book 5)

  (get-max-id xtdb-node)

  
  (xt/entity (xt/db xtdb-node) 12)
  
  xtdb-node
  (xt/status xtdb-node)
  
  (println
   (json/write-str
    (retreive-book 7)))

  (add-document nil {:testing 123 :entity/type :book})
  
  (xt/status xtdb-node)

  (list-all-books)

  (load-books-into-db xtdb-node)
  )