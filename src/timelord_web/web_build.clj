(ns timelord_web.web_build
  (:require [timelord_web.page_data :as pages]
            [timelord_db.timelord_auth.auth :as auth]
            [timelord_db.parse :as parse]
            [logging_interface.log :as log]
            [ring.util.response :as ring])
  (:gen-class))

;;Calls the functions that provide html to web at timelord.core via timelord_web.routes
;;web-build passes user data to auth if necessary, and calls basic pages
;;===============================================
;;============ ROUTING STATEMENTS ===============
;;===============================================
(defn login
  "Directs a user to the login page."
  []
  (pages/login))

(defn login-username-error
  "Directs a user to login page with the username error Div."
  []
  (pages/login (pages/username-error)))

(defn login-password-error
  "Directs a user to login page with password error Div."
  []
  (pages/login (pages/password-error)))

(defn login-invalid-password
  "Directs a user to login page with invalid password error Div."
  []
  (pages/login (pages/incorrect-password)))

(defn tracker
  [user]
  (pages/tracker user))

;;===============================================
;;========== END ROUTING STATEMENTS =============
;;===============================================

;;still needs to be fixed
(defn valid-login-with-db?
  "Consumes a credential map of username and supplied password.
  Uses Auth to validate credentials in the DB"
  [credential-map]
  (let [{:keys [username password]} credential-map]
    (cond
      ;;need to create auth/db-username?
      (not (auth/db-username? username)) (do (ring/redirect "/new-user-form") false)
      ;;need to create auth/db-password-matches and pages/incorrect-password to attach to login page
      (not (auth/db-password-matches? username password)) (do (ring/redirect "/login/invalid-password") false)
      :else (do (log/info ::check-login-with-db "Successful DB login." {:username username :metric 1 :tags ['http 'auth 'login]})
                true))))


(defn valid-credentials?
  "Consumes a credential map of username and supplied password. If credentials are valid returns true."
  [credential-map]
  (let [{:keys [username password]} credential-map]
    (cond
      (auth/invalid-username? username) (ring/redirect "login/username-error")
      (auth/invalid-password? password) (ring/redirect "login/password-error")
      :else (do (log/info ::check-login-form "Valid login credentials." {:username username :metric 1 :tags ['http 'auth 'login]})
                true))))


(defn check-login-credentials
  "Consumes a POST request from login form and validates credentials from users table in DB."
  [request]
  (let [credential-map (parse/login-parse request)
        username (:username credential-map)]
    (cond
      (not (valid-credentials? credential-map)) (log/error ::check-login-credentials "Login credentials invalid." {:tags ['http 'auth 'login]})
      (not (valid-login-with-db? credential-map)) (log/warn ::check-login-credentials "Credentials not found." {:tags ['http 'auth 'login]})
      :else (do
              (log/info ::check-login-credentials "Successful login moving to tracker." {:username username :tags ['http 'auth 'login 'success]})
              (ring/redirect (str "/tracker/" username))))))












;;;;Info log on startup
(log/info ::web_build "Web-builder online." {:metric 0 :tags ['info 'status]})

