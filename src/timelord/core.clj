(ns timelord.core
  (:require [timelord_web.web_build :as web-build]
            [timelord_web.routes :as routing]
            [timelord_web.page_data :as page-data]
            [compojure.core :refer :all]
            [hiccup.core :as hiccup])
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

