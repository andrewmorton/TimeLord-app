(ns timelord_web.web_build
  (:require [timelord_web.page_data :as pages]
            [timelord_db.timelord_auth.auth :as auth]
            [timelord_db.parse :as parse]
            [timelord_db.interface :as db-interface]
            [logging_interface.log :as log]
            [ring.util.response :as ring])
  (:gen-class))

;;Calls the functions that provide html to web at timelord.core via timelord_web.routes
;;web-build passes user data to auth if necessary, and calls basic pages
;;===============================================
;;============ ROUTING STATEMENTS ===============
;;===============================================
(defn login
  []
  (pages/login))

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
      (not (auth/db-username? username)) (do (pages/new-user) false)
      ;;need to create auth/db-password-matches and pages/incorrect-password to attach to login page
      (not (auth/db-password-matches? username password)) (do (pages/login (pages/incorrect-password)) false)
      :else (do (log/info ::check-login-with-db "Successful DB login." {:username username :metric 1 :tags ['http 'auth 'login]})
                true))))


(defn valid-credentials?
  "Consumes a credential map of username and supplied password. If credentials are valid returns true."
  [credential-map]
  (let [{:keys [username password]} credential-map]
    (cond
      (auth/invalid-username? username) (pages/login (pages/username-error))
      (auth/invalid-password? password) (pages/login (pages/password-error))
      :else (do (log/info ::check-login-form "Valid login credentials." {:username username :metric 1 :tags ['http 'auth 'login]})
                true))))


(defn check-login-credentials
  "Consumes a POST request from login form and validates credentials from users table in DB."
  [request]
  (let [{:keys [username] :as credential-map} (parse/login-parse request)]
    (cond
      (not (valid-credentials? credential-map)) (log/error ::check-login-credentials "Login credentials invalid." {:tags ['http 'auth 'login]})
      (not (valid-login-with-db? credential-map)) (log/error ::check-login-credentials "Login invalid in DB" {:tags ['http 'auth 'login]})
      :else (do (log/info ::check-login-credentials "Successful login moving to tracker." {:username username :tags ['http 'auth 'login 'success]})
                (ring/redirect (str "/tracker/" username))))))












;;;;Info log on startup
(log/info ::web_build "Web-builder online." {:metric 0 :tags ['info 'status]})

