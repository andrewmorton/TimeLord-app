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


(defn -main
  "Main Entry into the logging application"
  [& args]
  (println "Hello, World!"))

(defn basic-info-map
  "Creates a basic map for all loggers to use."
  []
  (let [timestamp (str (Date.))]
    {:host "localhost"
     :time timestamp}))

;;handler for logging information
;;still to come: sending data to a logging interface

(defn log-handler
  "Receives log output and directs it to file or otherwise."
  [type log-map]

  (if (= 'error type)
    (spit "error.log" (str "ERROR: " log-map "\n") :append true))
  (if (= 'info type)
    (spit "info.log" (str "INFO: " log-map "\n") :append true))
  (if (= 'trace type)
    (spit "trace.log" (str "TRACE: " log-map "\n") :append true))
  (if (= 'warn type)
    (spit "warnings.log" (str "WARN: " log-map "\n") :append true)))


;;functions for convenience. Log functions are returning a map by default to allow for possible integration with
;;a graphic interface later such as Riemann or Kibana.
;;These still need to be updated based on what each log is supposed to output, they are just copy/paste of info
;;so I have templates.

(defn info
  "Creates an info log entry."
  [service description additional]
  (let [log-map (merge (basic-info-map) {:service service :severity :info :description description} additional)]
    (log-handler 'info log-map)))

(defn warn
  "Creates a warning log entry."
  [service description additional]
  (let [log-map (merge (basic-info-map) {:service service :severity :warn :description description} additional)]
    (log-handler 'warn log-map)))

(defn trace
  "Creates a trace log entry."
  [service description additional]
  (let [log-map (merge (basic-info-map) {:service service :severity :trace :description description} additional)]
    (log-handler 'trace log-map)))

(defn error
  "Creates an error log entry."
  [service description additional]
  (let [log-map (merge (basic-info-map) {:service service :severity :error :description description} additional)]
    (log-handler 'error log-map)))
