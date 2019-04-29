(ns timelord_db.interface
  (:require [clojure.java.jdbc :as jdbc]
            [logging_interface.log :as log]
            [timelord_db.connection :as db]))

;;===============================================
;;============= Prepare and Execute =============
;;===============================================
(defn attach-random-int
  "Consumes a name and returns it with a random int from 1 to 10000.
  Should help reduce likelihood of functions preparing with the same name in PostgreSQL if put in front of the statement."
  [name]

  (let [int (int  (. Math floor (* 10000 (. Math random))))
        name (str name "_" int)]
    name))



;;===============================================
;;========= End Prepare and Execute =============
;;===============================================

;;===============================================
;;============ SELECT STATEMENTS ================
;;===============================================


(defn select-statement
  "Consumes a db spec, a table name, and the params needed to perform a select statement."
  [db column table value]

  (try (jdbc/query db [(str "SELECT " column " FROM " table " WHERE " column " = ?;") value])
       (catch Exception e
         (log/error ::select-username e {:metric 1 :tags ['select 'db 'query 'error]}))))

(defn select-username
  "Consumes a username and returns username from the DB."
  [username]

  (select-statement (db-pg-spec) "username" "timelord_user" username))










;;;;Info log on startup
(log/info ::db-interface "DB interface online." {:metric 0 :tags ['info 'status]})