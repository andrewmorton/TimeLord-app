(ns timelord_db.timelord_auth.auth
  (:require [timelord_db.interface :as db-interface]
            [logging_interface.log :as log]))

;;===============================================
;;==     Username and Password checking        ==
;;===============================================
(defn username-error
  "logs a username error."
  [service username]
  (log/error
    service
    "Username contains spaces or Specials."
    {:metric 1 :username username :tags ['http 'login 'username]}))

(defn password-error
  "logs a password error."
  [service]
  (log/error
    service
    "Password is less than 6 characters or lacks Specials."
    {:metric 1 :tags ['http 'login 'password]}))


(def reg-exp
  (fn [expression string]
    {:pre (string? string)}
    (re-find expression string)))

(def less-than-six?
  "Tests a string for at least 6 characters."
  (complement (partial reg-exp #".{6}")))

(def special-characters?
  "Tests a string and makes sure there are no special characters in it."
  (partial reg-exp #"\s|\W"))

(def no-special-characters?
  "tests a string and returns true if there are no special characters."
  (complement special-characters?))

(defn invalid-username?
  "Tests whether a username falls within parameters for the app."
  [username]
  (cond
    (less-than-six? username) (when (username-error ::invalid-username? username) true)
    (special-characters? username) (when (username-error ::invalid-username? username) true)
    :else nil))

;;Function: invalid-password
;;Creates a function that returns true if password is less than 6 chars or does NOT have a special character
(defn invalid-password?
  "Returns true if password is invalid."
  [password]
  (cond
    (less-than-six? password) (when (password-error ::invalid-password?) true)
    (no-special-characters? password) (when (password-error ::invalid-password?) true)
    :else nil))


;;===============================================
;;============= Auth for Web-Build ==============
;;===============================================

(defn db-username?
  "Consumes a username and returns true if the username record is found in the DB."
  [username]
  (let [username username
        username-db (db-interface/pg-select-username username)]
    (if (= username username-db) true (do (username-error ::db-username? username) false))))

(defn db-password-matches?
  "Consumes a password and returns true if the supplied password matches the password in the DB."
  [username password]
  (if (db-interface/pg-validate-password username password)
    true
    (when (password-error ::db-password-matches?) false)))

;;===============================================
;;============= End web-build Auth===============
;;===============================================

;;;;Info log on startup.
(log/info ::auth "Auth service online." {:metric 0 :tags ['info 'status]})