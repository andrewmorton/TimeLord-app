(ns timelord_db.interface
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str]
            [logging_interface.log :as log]
            [timelord_db.connection :as db]))


;;===============================================
;;============  DEFINING JDBC    ================
;;===============================================

(def jdbc-f
  (fn [f db statement]
    (f db statement)))

(defn pg-jdbc-f
  [f statement]
  (jdbc-f f (db/db-pg-spec) statement))

(def query-pg-f
  (partial pg-jdbc-f jdbc/query))

(def insert-pg-f
  (partial pg-jdbc-f jdbc/insert!))

(def update-pg-f
  (partial pg-jdbc-f jdbc/update!))

;;===============================================
;;============ SELECT STATEMENTS ================
;;===============================================

(defn select-f
  "Executes a select statement using jdbc/query in PG."
  [select-statement]
  {:pre [(re-find #"SELECT" (apply str select-statement))]}
  (query-pg-f select-statement))

(defn select-username
  "Selects a username from the DB. Returns a map of the username if it exists and nil if not."
  [username]
  (let [result (select-f ["SELECT username FROM timelord_user WHERE username = ?;" username])]
    (cond
      (empty? result) nil
      :else result)))

(defn select-password
  "Returns the hash of a user's password from the DB. If the password doesn't exist, returns nil."
  [username]
  (let [result (select-f ["SELECT password FROM timelord_user WHERE username = ?;" username])]
    (cond
      (empty? result) nil
      :else result)))

(defn hash-sql-string
  "Returns the sql vector for jdbc with username, hash, and password accounted for"
  [password hash username]
  ["SELECT (password = crypt(?, ?)) AS result FROM timelord_user WHERE username = ?;" password hash username])

(defn compare-hash
  "compares a user's provided password with the DB hash for that user's password."
  [username password]
  (let [{hash :password} (first (select-password username))
        {result :result} (first (select-f (hash-sql-string password hash username)))]
    (if result
      result
      nil)))


;Example of authentication:
;SELECT (pswhash = crypt('entered password', pswhash)) AS pswmatch FROM ... ;
;This returns true if the entered password is correct.

;;===============================================
;;=========== END SELECT STATEMENTS =============
;;===============================================









;;;;Info log on startup
(log/info ::db-interface "DB interface online." {:metric 0 :tags ['info 'status]})