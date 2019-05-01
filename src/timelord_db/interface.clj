(ns timelord_db.interface
  (:require [clojure.java.jdbc :as jdbc]
            [logging_interface.log :as log]
            [timelord_db.connection :as db]))

;;===============================================
;;============= Prepare and Execute =============
;;===============================================


;;later add functions to do true prepared statements using PostGres, not relying on JDBC

;;===============================================
;;========= End Prepare and Execute =============
;;===============================================

;;===============================================
;;============ SELECT STATEMENTS ================
;;===============================================


(defn select-statement
  "Consumes a DB spec, a table name, and the params needed to perform a select statement.
  Returns a clojure map of the result."
  [db column table value]
  (try (first (jdbc/query db [(str "SELECT " column " FROM " table " WHERE " column " = ?;") value]))
       (catch Exception e
         (log/error ::select-username e {:metric 1 :tags ['select 'db 'query 'error]}))))

(defn pg-select-username
  "Consumes a username and returns username column from the DB."
  [username]
  (select-statement (db/db-pg-spec) "username" "timelord_user" username))

(defn pg-select-password
  "Consumes a username and returns the DB value for user's password."
  [username]
  (select-statement (db/db-pg-spec) "password" "timelord_user" username))


;;===============================================
;;=========== END SELECT STATEMENTS =============
;;===============================================









;;;;Info log on startup
(log/info ::db-interface "DB interface online." {:metric 0 :tags ['info 'status]})