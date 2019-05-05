(ns timelord_db.connection
  (:gen-class))

(defn db-pg-spec
  "Returns a DB connection spec for Postgresql."
  []
  {:dbtype "postgresql"
   :dbname "timelordDB"
   :host "127.0.0.1"
   :user "timelord_app_user"
   :password "Old nail testing Blubbers!"})










