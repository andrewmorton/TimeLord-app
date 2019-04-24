(ns logging_interface.log
  (:import java.util.Date)
  (:gen-class))


;;;;Loggers follow a specific structure based on the criteria of Riemann to allow for later integration. Also because of
;;;;this, All log functions are returning a map of information.
;;;;This also will hopefully make troubleshooting easier in the future. All are subject to change.

;;basic event structure:
;{:host           host
;:service        current-function
;:severity       info, warning, etc
;:time           Current Time Stamp
;:description    "Description of the log event"
;:tags           list of strings, helps to filter log, ex [http, update, etc]
;:metric         integer associated with event
;:ttl            Time To Live for the event}

;;currently, logging writes to logging_interface/logs. This may not work in production, as logging_interface will be
;;a package and not accessible to a user. Will need to update (log-path) later to a directory that will be useful

(defn -main
  "Main Entry into the logging application"
  [& args]
  (println "Hello, World!"))

;;handler for logging information
;;still to come: sending data to a logging interface

(defn basic-log-map
  "Creates a basic map for all loggers to use."
  []
  (let [timestamp (str (Date.))]

    {:host "localhost"
     :time timestamp}))

;;define log paths relative to the project application

(defn log-path
  "Creates the default log path and takes a filename as an argument"
  [file-name]

  (str "./src/logging_interface/logs" file-name))

(def error-log-file
  (log-path "error.log"))

(def info-log-file
  (log-path "info.log"))

(def trace-log-file
  (log-path "trace.log"))

(def warn-log-file
  (log-path "warning.log"))

(defn log-string
  "Accepts a log-type and log-map and returns a log string"
  [log-type log-map]

  (str log-type ": " log-map "\n"))

(defn log-handler
"Receives log-map and directs it to file or otherwise."
[log-map]

(let [{:keys [severity] :as log-map} log-map]
;;wish-list: log-size
;;checks the size of log files and renames them once they get past a certain size
  (if (= :info severity)
    (spit info-log-file (log-string "INFO" log-map) :append true))

  (if (= :error severity)
    (spit error-log-file (log-string "ERROR" log-map) :append true))

  (if (= :trace severity)
    (spit trace-log-file (log-string "TRACE" log-map) :append true))

  (if (= :warn severity)
    (spit warn-log-file (log-string "WARN" log-map) :append true))))

;;functions for convenience. Log functions are returning a map by default to allow for possible integration with
;;a graphic interface later such as Riemann or Kibana.
;;These still need to be updated based on what each log is supposed to output, they are just copy/paste of info
;;so I have templates.

(defn info
  "Creates an info log-map entry."
  [service description additional]

  (let [log-map (merge (basic-log-map) {:service service :severity :info :description description} additional)]
    (log-handler log-map)))

(defn warn
  "Creates a warning log entry."
  [service description additional]
  (let [log-map (merge (basic-log-map) {:service service :severity :warn :description description} additional)]
    (log-handler log-map)))

(defn trace
  "Creates a trace log entry."
  [service description additional]

  (let [log-map (merge (basic-log-map) {:service service :severity :trace :description description} additional)]
    (log-handler log-map)))

(defn error
  "Creates an error log entry."
  [service description additional]

  (let [log-map (merge (basic-log-map) {:service service :severity :error :description description} additional)]
    (log-handler log-map)))

