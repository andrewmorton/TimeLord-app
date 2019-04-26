(ns timelord_db.connection
  (:require [clojure.java.jdbc :as jdbc]
            [logging_interface.log :as log])
  (:gen-class))

(def db-pg
  {:dbtype "postgresql"
   :dbname "timelordDB"
   :host "127.0.0.1"
   :user "timelord_app_user"
   :password "Old nail testing Blubbers!"})











