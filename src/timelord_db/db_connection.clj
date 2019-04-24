(ns timelord_db.db_connection
  (require [org.jdbi.jdbi-core :as Jdbi]
           [jdbi3-core/jdbi3-sqlobject :as sequel]
           [jdbi3-core/jdbi3-postgres :as postgres])
  (:gen-class))

;;in-memory DB for now, not sure what this means or if I would prefer to change it later.
(defn db
  "Connection to PostGreSQL DB using jdbi3."
  (.create Jdbi "jdbc:postgres:mem:test"))