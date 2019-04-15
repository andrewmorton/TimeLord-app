(ns timelord_web.web_build
  (:require [timelord_web.page_data :as pages]
            [timelord_db.timelord_auth.auth :as auth]
            [logging_interface.log :as log]
            [ring.util.response :as ring]))

;;Calls the functions that provide html to web at timelord.core via timelord_web.routes
;;web-build passes user data to auth if necessary, and calls basic pages

(defn login
  []
  (pages/login))

(defn tracker
  [user]
  (pages/tracker user))

(defn check-login-form
  "Calls auth function to validate username and password and returns an HTTP redirect for
  /tracker/:user if username and password have passed."
  [request]

  (let [result (auth/validate-login-credentials request)
        username (:username result)
        password (:password result)]
    (if (nil? username)
      (pages/login (pages/username-error)))
    (if (nil? password)
      (pages/login (pages/password-error)))

    (log/info ::check-login-form "Successful login." {:username username})
    (ring/redirect (str "/tracker/" username))))

