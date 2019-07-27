(ns timelord_db.timelord_auth.auth
  (:require [timelord_db.interface :as db-interface]
            [logging_interface.log :as log]))

;;===============================================
;;==     Username and Password checking        ==
;;===============================================
(defn username-error
  "logs a username error."
  [service string username]
  (log/error
    service
    string
    {:metric 1 :username username :tags ['http 'login 'username]}))

(defn password-error
  "logs a password error."
  [service string]
  (log/error
    service
    string
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
    (less-than-six? username) (username-error ::invalid-username? "Less than six characters." username)
    (special-characters? username) (username-error ::invalid-username? "Contains Special Characters." username)
    :else nil))

;;Function: invalid-password
;;Creates a function that returns true if password is less than 6 chars or does NOT have a special character
(defn invalid-password?
  "Returns true if password is invalid."
  [password]
  (cond
    (less-than-six? password) (password-error ::invalid-password? "Less than six characters.")
    (no-special-characters? password) (password-error ::invalid-password? "No Special Characters.")
    :else nil))


;;===============================================
;;============= Auth for Web-Build ==============
;;===============================================

(defn db-username?
  "Consumes a username and returns true if the username record is found in the DB."
  [username]
  (let [username username
        username-db (db-interface/select-username username)]
    (if-not (and username username-db)
      (do (username-error ::db-username? "Username not in Database." username) nil)
      true)))

(defn db-password-matches?
  "Consumes a password and returns true if the supplied password matches the password in the DB."
  [username password]
  (if-not (db-interface/compare-hash username password)
    (do (password-error ::db-password-matches? "Password doesn't match Database record.") nil)
    true))

;;===============================================
;;============= End web-build Auth===============
;;===============================================

;;;;Info log on startup.
(log/info ::auth "Auth service online." {:metric 0 :tags ['info 'status]})