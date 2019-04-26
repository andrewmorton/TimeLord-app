(ns logging_interface.log
  (:import java.util.Date)
  (:gen-class))


;;;;Loggers follow a specific structure based on the criteria of Riemann to allow for later integration. Also because of
;;;;this, All log functions are returning a map of information.
;;;;This also will hopefully make troubleshooting easier in the future. All are subject to change.

;;basic event structure:
;{:host           host
;:service        current-function, use ::(function name)
;:severity       info, warning, etc
;:time           Current Time Stamp
;:description    "Description of the log event"
;;metric         Metric describing the event. Difficult to be descriptive, use 0 for success and most info log entries
;:tags           list of strings, helps to filter log, ex [http, update, etc]
;:ttl            Time To Live for the event (if necessary)
;;etc            any other key that would be useful}

;;currently, logging writes to logging_interface/logs. This may not work in production, as logging_interface will be
;;a package and not accessible to a user. Will need to update (log-path) later to a directory that will be useful


;;===============================================================
;;================== Build Log entries ==========================
;;===============================================================
;;Creating the basic log map
;;ALL LOGS SHOULD USE THIS BASIC MAP
(defn log-entry
  "Creates a basic map for all loggers to use."
  [[service severity description]]
  (let [timestamp (str (Date.))
        service service
        severity severity
        description description]

    {:host "localhost"
     :time timestamp
     :service service
     :severity severity
     :description description}))

;;log entry types
(defn info-log-entry
  "Consumes a service and description and returns a basic info log entry."
  [[service description]]

  (log-entry [service ':info description]))

(defn error-log-entry
  "Consumes a service and description and returns a basic error log entry."
  [[service description]]

  (log-entry [service ':error description]))

(defn warn-log-entry
  "Consumes a service and description and returns a basic warning log entry."
  [[service description]]

  (log-entry [service ':warn description]))

(defn trace-log-entry
  "Consumes a service or a service and description and returns a basic trace log entry."
  [[service description]]

  (log-entry [service ':trace description]))

;;Final formatted log string
(defn log-string
  "Accepts a log-type and log-map and returns a log string to be written to file."
  [log-type log-map]

  (str log-type ": " log-map "\n"))


;;===============================================================
;;=================== End log entries ===========================
;;===============================================================


;;===============================================================
;;============ Logger types - Create new loggers ================
;;===============================================================


;;File logger -> accepts a log entry and writes it to a file
(defn file-logger
  "Consumes a file name and entry and writes the entry to file.
   Appends by default and renames if the file gets too big."
  [file-name entry]
  ;;wish list: check-file-size consumes a file and renames it with a times stamp if > 1MB.
  (spit file-name entry :append true))

;;Creating log file paths
;;define log path relative to the project application
(defn log-path
  "Creates the default log path and takes a filename as an argument"
  [file-name]

  (str "./src/logging_interface/logs/" file-name))

;;File paths for each log type
(def error-log-file
  (log-path "error.log"))

(def info-log-file
  (log-path "info.log"))

(def trace-log-file
  (log-path "trace.log"))

(def warn-log-file
  (log-path "warning.log"))

(defn info-file-logger
  "Consumes a log entry and spits to info-log-file."
  [entry]

  (file-logger info-log-file (log-string "INFO" entry)))

(defn error-file-logger
  "Consumes a log entry and spits to error-log-file."
  [entry]

  (file-logger error-log-file (log-string "ERROR" entry)))

(defn warn-file-logger
  "Consumes a log entry and spits to warn-log-file."
  [entry]

  (file-logger warn-log-file (log-string "WARN" entry)))

(defn trace-file-logger
  "Consumes a log entry and spits to trace-log-file"
  [entry]

  (file-logger trace-log-file (log-string "TRACE" entry)))

;;==============================================================
;;================== End logger types ==========================
;;==============================================================


;;functions for convenience. Log functions are returning a map by default to allow for possible integration with
;;a graphic interface later such as Riemann or Kibana.
(defn info
  "Consumes a service and a description and additional information and sends an info log map to info-file-logger."
  [service description {:as additional}]

  (let [log-body (info-log-entry [service description])
        log-additional additional
        log-map (merge log-body log-additional)]

    (info-file-logger log-map)))

(defn warn
  "Consumes a service and a description and additional information and sends a warning log map to warn-file-logger."
  [service description {:as additional}]
  (let [log-body (warn-log-entry [service description])
        log-additional additional
        log-map (merge log-body log-additional)]

    (warn-file-logger log-map)))

(defn trace
  "Consumes a service and optionally a description or additional parameters and sends a trace log map to
  trace-file-logger."
  ([service description {:as additional}]
   (let [log-body (trace-log-entry [service description])
         log-additional additional
         log-map (merge log-body log-additional)]

     (trace-file-logger log-map)))

  ([service]
   (trace service "Made it." {:metric 1 :tags ['trace 'debug]})))

(defn error
  "Consumes a service and description and additional parameters and sends an error log map to error-file-logger."
  [service description {:as additional}]

  (let [log-body (error-log-entry [service description])
        log-additional additional
        log-map (merge log-body log-additional)]

    (info ::error "Recorded an Error, check error logs." {:metric 1 :tags ['error 'debug 'info]})
    (error-file-logger log-map)))




;;;;Info log on startup
(info ::log "Logging online." {:metric 0 :tags ['logging 'info 'service-status]})


