(ns global-java-interop.global-variables
  (import (java.time.LocalDate)
          (java.time.LocalTime))
  (:gen-class))

(defn local-date
  "Returns a current local date value"
  []

 (let [date (LocalDate/now)]
   date))

(defn current-time
  "Returns the current local time, uses regexp to parse out HH:MM:SS."
  []

  (let [time (re-find #"..:..:.." (.toString (LocalTime/now)))]
    time))

