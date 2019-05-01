(ns timelord_db.timelord_auth.auth
  (:require [timelord_web.web_build :as web-build]
            [timelord_db.parse :as parse]
            [timelord_db.interface :as db-interface]
            [logging_interface.log :as log]))

;;===============================================
;;============= General Exceptions===============
;;===============================================



;;===============================================
;;============= Auth for Web-Build ==============
;;===============================================
;;logging functions for username and password errors
(defn log-username-error
  "Logs a username specific error to error.log."
  [username]
  (log/error ::validate-login-credentials "Invalid Username." {:username username :tags ['auth 'http 'login]}))

(defn log-password-error
  "Logs a password specific error to error.log"
  []
  (log/error ::validate-login-credentials "Invalid Password." {:tags ['auth 'http 'login]}))
;;

(defn invalid-username?
  "Returns true if username is invalid."
  [username]
  (cond
    (re-find #"\s|\W" username) (do (log-username-error username) true)
    (re-find #".{4}" username) (do (log-username-error username) true)
    :else false))

;;Function: invalid-password
;;Creates a function that returns true if password is less than 6 chars or does NOT have a special character
(defn invalid-password?
  "Returns true if password is invalid."
  [password]
  (cond
    (not (re-find #".{6}" password)) (do (log-password-error) true)
    (not (re-find #"\W" password)) (do (log-password-error) true)
    :else false))

(defn check-in-username
  "Consumes a user supplied password and a DB hash for the same user.
  Returns true if the password matches."
  [user-password db-password])


;;===============================================
;;============= End web-build Auth===============
;;===============================================

;;;;Info log on startup.
(log/info ::auth "Auth service online." {:metric 0 :tags ['info 'status]})