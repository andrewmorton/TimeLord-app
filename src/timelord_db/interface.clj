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

(defn execute
  "Executes a prepared statement."
  [referring values]

  (try
    (jdbc/query db/db-pg-spec ["EXECUTE " referring values])
    (catch Exception e (log/error ::execute e {:metric 1 :tags ['db 'execute 'error]}))))

(defn prepare
  "Consumes a SQL statement and the name of a referring function and prepares PostgreSQL to execute the statement"
  [referring statement values]
  (let [referring (attach-random-int referring)
        statement statement
        values values]
    (try
      (jdbc/query db/db-pg-spec ["PREPARE " referring statement])
      (catch Exception e
        (log/error ::prepare e {:metric 1 :tags ['prepare 'error 'db]})))
    (if-not Exception
      (execute referring values))))

;;===============================================
;;========= End Prepare and Execute =============
;;===============================================













;;;;Info log on startup
(log/info ::db-interface "DB interface online." {:metric 0 :tags ['info 'status]})