(ns timelord_web.routes
  (:require [timelord_web.web_build :as web-build]
            [logging_interface.log :as log]
            [compojure.core :refer :all]
            [compojure.route :as route])
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
  (route/not-found (do (log/error ::route "Not Found" {:metric 1 :tags ['error 'http 'routing 'not-found]})
                       "Not Found.")))

;;allows form parameters to come through HTTP request
(def timelord
  (wrap-params timelord-routes))

(defn run-timelord
  ([port]
   (try
     (run-jetty timelord {:port port})
     (catch Exception e (log/error ::run-timelord e {:metric 1 :tags ['error 'http 'routing 'port]}))))


  ([]
   (run-jetty timelord {:port 8080})))



;;;;Info log on startup
(info ::routes "Routing service online." {:metric 0 :tags ['info 'status]})