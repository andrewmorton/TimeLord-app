(ns timelord_web.web_build
  (:require [timelord_web.page_data :as content]
            [timelord_db.timelord_auth.auth :as auth]
            [timelord_db.parse :as parse]
            [logging_interface.log :as log]
            [ring.util.response :as ring]
            [hiccup.page :as page])
  (:gen-class))

;;Calls the functions that provide html to web at timelord.core via timelord_web.routes
;;web-build passes user data to auth if necessary, and calls basic pages
;;===============================================
;;============ LAYOUT STATEMENTS ===============
;;===============================================

(defn app-view
  "View app for Timelord."
  [& contents]
  (page/html5
    contents))

;;===============================================
;;========== HTTP Map functions     =============
;;===============================================


;;===============================================
;;========== End HTTP Map functions =============
;;===============================================



(defn check-login-credentials
  "Consumes a POST request from login form and validates credentials from users table in DB."
  [request]
  (let [credential-map (parse/login-parse request)
        username (:username credential-map)
        password (:password credential-map)]
    (try
      (cond
        (auth/invalid-username? username) (ring/redirect "/login/username-error")
        (auth/invalid-password? password) (ring/redirect "/login/password-error")
        (not (auth/db-username? username)) (ring/redirect (str "/new-user-form/" username))
        (not (auth/db-password-matches? username password)) (ring/redirect "/login/invalid-password")
        :else (assoc (ring/redirect (str "/tracker/" username)) :session username))
      (catch Exception e
        (let [e (Throwable->map e)]
          (log/error ::check-login-credentials
                     "Error logging in user"
                     {:metric 1 :cause e :tags ['http 'login 'error]})
          (assoc (ring/redirect "/error") :exception e))))))

(defn new-user-credentials
  "Checks new user credentials. Sends a map to DB interface to add the user if valid."
  [request]
  (let [check-login-map (check-login-credentials request)
        checked-headers (:headers check-login-map)
        location (get checked-headers "Location")]
    (if (re-find #"new-user-form" location))))
      ;;then send full parameter map to auth to be checked into the db
      ;;then alert the user that creation was successful and send the user to their own tracker)))















;;;;Info log on startup
(log/info ::web_build "Web-builder online." {:metric 0 :tags ['info 'status]})

