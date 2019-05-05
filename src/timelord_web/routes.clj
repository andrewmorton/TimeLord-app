(ns timelord_web.routes
  (:require [timelord_web.web_build :as web-build]
            [logging_interface.log :as log]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as ring]
            [ring.middleware.params :as mid-ring]
            [ring.adapter.jetty :as jetty]))


(defroutes timelord-routes; Routes for TimeLord app
 "Routes communication to the other pages of the web app"

  (GET "/"
    []
    (ring/redirect "/login"))

  (GET "/login"
    []
    (web-build/login))

  (GET "/login/username-error"
       []
    (web-build/login-username-error))

  (GET "/login/password-error"
       []
    (web-build/login-password-error))

  (GET "/login/invalid-password"
       []
    (web-build/login-invalid-password))

  ; (GET "/control-center"
  ;   []
  ;   (build/control-center))

  (GET "/tracker/:user" [user]
    (web-build/tracker user))

  (POST "/check-login" request
    (web-build/check-login-credentials request))
  
  (route/resources "/")
  (route/not-found (do (log/error ::route "Not Found" {:metric 1 :tags ['error 'http 'routing 'not-found]})
                       "Not Found.")))

;;allows form parameters to come through HTTP request
(def timelord
  (mid-ring/wrap-params timelord-routes))

(defn run-timelord
  ([port]
   (try
     (jetty/run-jetty timelord {:port port})
     (catch Exception e (log/error ::run-timelord e {:metric 1 :tags ['error 'http 'routing 'port]}))))


  ([]
   (jetty/run-jetty timelord {:port 8080})))



;;;;Info log on startup
(log/info ::routes "Routing service online." {:metric 0 :tags ['info 'status]})