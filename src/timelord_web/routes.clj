(ns timelord_web.routes
  (:require [timelord_web.web_build :as web-build]
            [timelord_web.page_data :as content]
            [logging_interface.log :as log]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as ring]
            [ring.middleware.session :as mid-session]
            [ring.middleware.params :as mid-ring]
            [ring.adapter.jetty :as jetty]))


(defroutes timelord-routes; Routes for TimeLord app
 "Routes communication to the other pages of the web app"

  (GET "/"
    []
    (ring/redirect "/login"))

  (GET "/login"
    []
    (web-build/app-view (content/login)))

  (GET "/login/username-error"
       []
    (web-build/app-view (content/login) (content/username-error)))

  (GET "/login/password-error"
       []
    (web-build/app-view (content/login) (content/password-error)))

  (GET "/login/invalid-password"
       []
    (web-build/app-view (content/login) (content/incorrect-password)))

  (GET "/new-user-form/:username" [username]
    (web-build/app-view (content/new-user-page username)))

  ; (GET "/control-center"
  ;   []
  ;   (build/control-center))

  (GET "/tracker/:user" [user :as request]
    (if (= user (:session request))
      (web-build/app-view (content/tracker user))
      (ring/redirect "/login")))


  (POST "/check-login" request
    (web-build/check-login-credentials request "/tracker/"))

  (POST "/create-new-user/" request
    (web-build/check-login-credentials request "/tracker/"))

  (GET "/error" request
    (web-build/app-view (content/error-page (:exception request))))

  
  (route/resources "/")

  (route/not-found (do (log/error ::route "Not Found" {:metric 1 :tags ['error 'http 'routing 'not-found]})
                       "Not Found.")))

;;allows form parameters to come through HTTP request
(def timelord
  (mid-session/wrap-session
    (mid-ring/wrap-params timelord-routes)
    {:cookie-attrs {:max-age 3600}}))

(defn run-timelord
  ([port]
   (try
     (jetty/run-jetty timelord {:port port})
     (catch Exception e (log/error ::run-timelord e {:metric 1 :tags ['error 'http 'routing 'port]}))))
  ([]
   (jetty/run-jetty timelord {:port 8080})))



;;;;Info log on startup
(log/info ::routes "Routing service online." {:metric 0 :tags ['info 'status]})