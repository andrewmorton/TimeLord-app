(ns timelord.core
  (:require [timelord_web.routes :as routing]
            [timelord_web.page_data :as pages]
            [timelord_web.web_build :as web-build]
            [timelord_db.connection :as db-connection]
            [timelord_db.interface :as db-interface]
            [timelord_db.parse :as db-parse]
            [timelord_db.schema :as db-schema]
            [timelord_db.timelord_auth.auth :as auth]
            [logging_interface.log :as log])

  (:gen-class))
;;;;Currently developing using lein ring server-headless in CLI. timelord_web.routes/timelord-routes is listed as
;;;;Ring handler, will run server on localhost:3000 for testing/building.
(defn -main
  "Main Entry to Timelord"
  []
  (println "Main Entry successful"))

(defn start
  "Starts Server for Timelord application on port specified default is 8080"
  ([port]
   (routing/run-timelord port))

  ([]
   (routing/run-timelord)))




;;;;Info log on startup.
(info ::core "Core services online." {:metric 0 :tags ['info 'status]})

