(ns timelord_web.routes
  (:require [timelord_web.web_build :as web-build]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [timelord_db.timelord_auth.auth :as auth])
  (:use ring.util.response
        ring.adapter.jetty
        ring.middleware.params))


(defroutes timelord-routes; Routes for TimeLord app
 "Routes communication to the other pages of the web app"

  (GET "/"
    []
    (web-build/login))

  ; (GET "/control-center"
  ;   []
  ;   (build/control-center))

  (GET "/tracker/:user" [user]
    (web-build/tracker user))

  (POST "/check-login" request
    (web-build/check-login-form request))
  
  (route/resources "/")
  (route/not-found "Not Found"))


(def timelord
  (wrap-params timelord-routes))

(defn run-timelord
  ([port]
   (run-jetty timelord {:port port}))

  ([]
   (run-jetty timelord {:port 8080})))