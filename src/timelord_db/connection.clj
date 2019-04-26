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
  (jdbc/db-connection db-pg-spec)
  (catch Exception e
    (log/error ::db-connection "Error connecting to Database." {:metric 1 :error-text e :tags ['db 'error 'connect]})))










