(ns timelord_db.parse)

;;a parsing interface for TimeLord, converts form data into a map to be sent
;;to the rest of the application

;;open up check-user parameters and create a map for check-user

(defn login-parse
  "Pulls username and password out of login page.
  Ignores all other arguments.
  If no argument given, throws error."
  ([params]
    {:userName (params "userName") :password (params "password")})

  ([]
    (throw (Exception. "No params given to login-parse"))))