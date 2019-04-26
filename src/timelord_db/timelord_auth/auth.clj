(ns timelord_db.timelord_auth.auth
  (:require [timelord_db.parse :as parse]
            [logging_interface.log :as log]))

;;Function: regexp-bool
;;Create a function that will return true or false in the case of a regexp
;;if a string is returned, return true
(defn regexp-bool
  "Takes a regular expression output as argument.
  If regular expression returns a string or a vector then a match was found and returns true."
  [output]
  
  (if (or (string? output) (vector? output))
    true
    false))

;;Function: invalid-username
;;Creates a function that returns true if username has less than 4 chars, contains spaces, is empty,
;;or has special characters
;;
(defn invalid-username
  "Returns true if username matches parameters."
  [username]

  (if (regexp-bool (re-find #"\s|\W" username))
    true
    false)
  (if-not (regexp-bool (re-find #".{4}" username))
    true
    false))


;;Function: invalid-password
;;Creates a function that returns true if password is less than 6 chars or does NOT have a special character
;;
(defn invalid-password
  "Returns true if password matches parameters."
  [password]

  (if-not (or (regexp-bool (re-find #".{6}" password))
              (regexp-bool (re-find #"\W" password)))
    true
    false))



(defn validate-login-credentials
  "Expects a full HTTP request and pulls out params. Verifies information provided via login form.
  If username and password are within app rules sends username and password in map to Database to be verified.
  If Username exists and DB interface clears password then directs to Tracker form.
  If Username does not exist redirects to create-new-user form."
  [{params :params}]

  ;;db-auth/invalid-username or invalid-password return true if criteria match.
  ;;Function will send the user back to login with the appropriate error displayed. In theory, this functionality should
  ;;never have to be used, since JavaScript should perform validation of the form on submit.
  (let [login-map (parse/login-parse params)
        username (:userName login-map)
        password (:password login-map)]

    (if (invalid-username username)
      (do (log/error ::validate-login-credentials "Invalid Username." {:username username :tags ['auth 'http 'login]})
          {:username nil :password nil})
      (if (invalid-password password)
        (do (log/error ::validate-login-credentials "Invalid Password." {:tags ['auth 'http 'login]})
            {:username nil :password nil})
        {:username username}))))






;;;;Info log on startup.
(info ::auth "Auth service online." {:metric 0 :tags ['info 'status]})