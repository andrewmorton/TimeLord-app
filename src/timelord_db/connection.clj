(ns timelord_db.connection
  (:require [logging_interface.log :as log]
            [clojure.java.jdbc :as jdbc])
  (:gen-class))

(defn db-pg-spec
  "Returns a DB connection spec for Postgresql."
  []
  {:dbtype "postgresql"
   :dbname "timelordDB"
   :host "127.0.0.1"
   :user "timelord_app_user"
   :password "Old nail testing Blubbers!"})

(try
  (jdbc/db-connection (db-pg-spec))
  (catch Exception e
    (let [{error :cause} e]
      (log/error ::db-connection "Error connecting to Database." {:metric 1 :cause error :tags ['db 'error 'connect]}))))










